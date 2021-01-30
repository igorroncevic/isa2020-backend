package team18.pharmacyapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team18.pharmacyapp.helpers.DateTimeHelpers;
import team18.pharmacyapp.model.Term;
import team18.pharmacyapp.model.WorkSchedule;
import team18.pharmacyapp.model.dtos.*;
import team18.pharmacyapp.model.exceptions.*;
import team18.pharmacyapp.model.users.Doctor;
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
    public List<PharmacyMarkPriceDTO> getPharmaciesWithAvailableCounselings(DateTimeRangeDTO timeRange) throws BadTimeRangeException {
        List<PharmacyMarkPriceDTO> allPharmacies = counselingRepository.getPharmaciesWithAvailableCounselings(timeRange.getFromTime(), timeRange.getToTime());

        List<PharmacyMarkPriceDTO> availablePharmacies = new ArrayList<>();

        Date today = new Date(System.currentTimeMillis() + 60 * 1000);
        if(today.after(timeRange.getFromTime()) || today.after(timeRange.getToTime())) throw new BadTimeRangeException("");

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
    @Transactional(rollbackFor = {AlreadyScheduledException.class, ScheduleTermException.class, RuntimeException.class})
    public boolean patientScheduleCounseling(ScheduleCounselingDTO term) throws AlreadyScheduledException, ScheduleTermException, RuntimeException {
        Term checkTerm = counselingRepository.checkIfPatientHasCounselingWithDoctor(term.getPatientId(), term.getDoctorId(), new Date());

        if(checkTerm != null) throw new AlreadyScheduledException("You already have a counseling with this pharmacist.");

        UUID id = UUID.randomUUID();
        int retVal = counselingRepository.patientScheduleCounseling(id, term.getPatientId(),
                term.getDoctorId(), term.getFromTime(), term.getToTime());

        if (retVal != 1) throw new ScheduleTermException("Could not save this counseling");

        String userMail = "savooroz33@gmail.com";   // zakucano za sada
        String subject = "[ISA Pharmacy] Confirmation - Counseling scheduling";
        String body = "You have successfully scheduled a counseling on our site.\n" +
                "Your reservation ID: " + id.toString();
        new Thread(() -> emailService.sendMail(userMail, subject, body)).start();

        return true;
    }

    @Override
    @Transactional(rollbackFor = {EntityNotFoundException.class, ActionNotAllowedException.class, RuntimeException.class})
    public boolean patientCancelCounseling(CancelCounselingDTO term) throws EntityNotFoundException, ActionNotAllowedException, RuntimeException {
        Term checkTerm = counselingRepository.findByIdCustom(term.getCounselingId());

        if (checkTerm == null) throw new EntityNotFoundException("There is no such counseling");
        if(!checkTerm.getPatient().getId().equals(term.getPatientId())) throw new ActionNotAllowedException("You can only cancel your own counselings");

        Date yesterday = new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000);
        if (checkTerm.getStartTime().before(yesterday)) throw new ActionNotAllowedException("Cannot cancel 24hrs before the counseling or any past counselings");

        int rowsUpdated = counselingRepository.patientCancelCounseling(term.getCounselingId());
        if (rowsUpdated != 1) throw new RuntimeException("Couldn't cancel this term!");

        return true;
    }

}
