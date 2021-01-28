package team18.pharmacyapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team18.pharmacyapp.helpers.DateTimeHelpers;
import team18.pharmacyapp.model.Term;
import team18.pharmacyapp.model.WorkSchedule;
import team18.pharmacyapp.model.dtos.DateTimeRangeDTO;
import team18.pharmacyapp.model.dtos.DoctorDTO;
import team18.pharmacyapp.model.dtos.PharmacyMarkPriceDTO;
import team18.pharmacyapp.model.dtos.ScheduleCounselingDTO;
import team18.pharmacyapp.model.enums.TermType;
import team18.pharmacyapp.model.exceptions.ActionNotAllowedException;
import team18.pharmacyapp.model.exceptions.ScheduleTermException;
import team18.pharmacyapp.model.users.Doctor;
import team18.pharmacyapp.model.users.Patient;
import team18.pharmacyapp.repository.CounselingRepository;
import team18.pharmacyapp.repository.DoctorRepository;
import team18.pharmacyapp.service.interfaces.CounselingService;
import team18.pharmacyapp.service.interfaces.EmailService;

import java.time.LocalTime;
import java.util.*;

@Service
public class CounselingServiceImpl implements CounselingService {
    private final CounselingRepository counselingRepository;
    private final DoctorRepository doctorRepository;
    private final EmailService emailService;

    @Autowired
    public CounselingServiceImpl(CounselingRepository counselingRepository, DoctorRepository doctorRepository, EmailService emailService) {
        this.counselingRepository = counselingRepository;
        this.doctorRepository = doctorRepository;
        this.emailService = emailService;
    }

    @Override
    public List<PharmacyMarkPriceDTO> getPharmaciesWithAvailableCounselings(DateTimeRangeDTO timeRange) {
        List<PharmacyMarkPriceDTO> allPharmacies = counselingRepository.getPharmaciesWithAvailableCounselings(timeRange.getFromTime(), timeRange.getToTime());

        List<PharmacyMarkPriceDTO> availablePharmacies = new ArrayList<>();

        for (PharmacyMarkPriceDTO p : allPharmacies) {
            List<DoctorDTO> doctors = this.getFreeDoctorsForPharmacy(p.getId(), timeRange);
            if (doctors.size() >= 1) availablePharmacies.add(p);
        }

        return availablePharmacies;
    }

    @Override
    public List<DoctorDTO> getFreeDoctorsForPharmacy(UUID pharmacyId, DateTimeRangeDTO timeRange) {
        List<Doctor> doctors = doctorRepository.findAllPharmacistsInPharmacy(pharmacyId);
        List<DoctorDTO> freeDoctors = new ArrayList<>();

        for (Doctor d : doctors) {
            LocalTime timeRangeStartTime = DateTimeHelpers.getTimeWithoutDate(timeRange.getFromTime());
            LocalTime timeRangeEndTime = DateTimeHelpers.getTimeWithoutDate(timeRange.getToTime());

            Date timeRangeStartDate = DateTimeHelpers.getDateWithoutTime(timeRange.getFromTime());
            Date timeRangeEndDate = DateTimeHelpers.getDateWithoutTime(timeRange.getToTime());

            boolean continueFlag = false;

            for (WorkSchedule ws : d.getWorkSchedules()) {
                LocalTime wsStartTime = DateTimeHelpers.getTimeWithoutDate(ws.getFromHour());
                LocalTime wsEndTime = DateTimeHelpers.getTimeWithoutDate(ws.getToHour());

                Date wsStartDate = DateTimeHelpers.getDateWithoutTime(ws.getFromHour());
                Date wsEndDate = DateTimeHelpers.getDateWithoutTime(ws.getToHour());

                // !date.isBefore === isAfterOrEqual
                // !date.isAfter === isBeforeOrEqual
                if (!(!timeRangeStartTime.isBefore(wsStartTime) && !timeRangeEndTime.isAfter(wsEndTime)
                        && !timeRangeStartDate.before(wsStartDate) && !timeRangeEndDate.after(wsEndDate))) {
                    continueFlag = true; // Vrijeme pocetka i kraja moraju biti unutar radnog vremena.
                }
            }

            if (continueFlag) continue; // Ako je predlozeno vrijeme van radnog vremena

            List<Term> doctorsCounselings = counselingRepository.findAllCounselingsForDoctor(d.getId());
            for (Term t : doctorsCounselings) {
                if (DateTimeHelpers.checkIfTimesIntersect(t.getStartTime(), t.getEndTime(), timeRange.getFromTime(), timeRange.getToTime())) {
                    continueFlag = true;
                    break;
                }
            }

            if (continueFlag) continue; // Ako se vrijeme nekog termina poklapa sa ovim terminom

            DoctorDTO doctorDTO = new DoctorDTO();
            doctorDTO.setId(d.getId());
            doctorDTO.setName(d.getName());
            doctorDTO.setSurname(d.getSurname());
            float averageMark = doctorRepository.getAverageMarkForDoctor(d.getId());
            doctorDTO.setAverageMark(averageMark);

            freeDoctors.add(doctorDTO); // Ako nista od ovoga nije tacno, doktor je slobodan
        }

        return freeDoctors;
    }

    @Override
    @Transactional(rollbackFor = {ScheduleTermException.class, RuntimeException.class})
    public boolean patientScheduleCounseling(ScheduleCounselingDTO term) throws ScheduleTermException, RuntimeException {
        UUID id = UUID.randomUUID();
        int retVal = counselingRepository.patientScheduleCounseling(id, term.getPatientId(),
                term.getDoctorId(), term.getFromTime(), term.getToTime());

        if(retVal != 1) throw new ScheduleTermException("Could not save this counseling");

        String userMail = "savooroz33@gmail.com";   // zakucano za sada
        String subject = "[ISA Pharmacy] Confirmation - Counseling scheduling";
        String body = "You have successfully scheduled a counseling on our site.\n" +
                "Your reservation ID: " + id.toString();
        new Thread(() -> emailService.sendMail(userMail, subject, body)).start();

        return true;
    }

}
