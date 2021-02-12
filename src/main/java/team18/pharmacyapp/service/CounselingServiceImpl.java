package team18.pharmacyapp.service;

import org.hibernate.annotations.Loader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team18.pharmacyapp.helpers.DateTimeHelpers;
import team18.pharmacyapp.model.Term;
import team18.pharmacyapp.model.WorkSchedule;
import team18.pharmacyapp.model.dtos.*;
import team18.pharmacyapp.model.enums.TermType;
import team18.pharmacyapp.model.exceptions.*;
import team18.pharmacyapp.model.users.Doctor;
import team18.pharmacyapp.model.users.Patient;
import team18.pharmacyapp.repository.CounselingRepository;
import team18.pharmacyapp.repository.MarkRepository;
import team18.pharmacyapp.repository.users.DoctorRepository;
import team18.pharmacyapp.repository.users.PatientRepository;
import team18.pharmacyapp.service.interfaces.CounselingService;
import team18.pharmacyapp.service.interfaces.EmailService;
import team18.pharmacyapp.service.interfaces.TermService;

import javax.persistence.LockModeType;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class CounselingServiceImpl implements CounselingService {
    private final CounselingRepository counselingRepository;
    private final DoctorRepository doctorRepository;
    private final EmailService emailService;
    private final MarkRepository markRepository;
    private final PatientRepository patientRepository;
    private final TermService termService;

    @Autowired
    public CounselingServiceImpl(CounselingRepository counselingRepository, DoctorRepository doctorRepository, EmailService emailService, MarkRepository markRepository, PatientRepository patientRepository, TermService termService) {
        this.counselingRepository = counselingRepository;
        this.doctorRepository = doctorRepository;
        this.emailService = emailService;
        this.markRepository = markRepository;
        this.patientRepository = patientRepository;
        this.termService = termService;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PharmacyMarkPriceDTO> getPharmaciesWithAvailableCounselings(DateTimeRangeDTO timeRange) throws BadTimeRangeException {
        List<PharmacyMarkPriceDTO> allPharmacies = counselingRepository.getPharmaciesWithAvailableCounselings(timeRange.getFromTime(), timeRange.getToTime());

        List<PharmacyMarkPriceDTO> availablePharmacies = new ArrayList<>();

        Date today = new Date(System.currentTimeMillis() + 60 * 1000);
        if (today.after(timeRange.getFromTime()) || today.after(timeRange.getToTime()))
            throw new BadTimeRangeException("");

        for (PharmacyMarkPriceDTO p : allPharmacies) {
            List<DoctorMarkPharmaciesDTO> doctors = this.getFreeDoctorsForPharmacy(p.getId(), timeRange);
            if (doctors.size() >= 1) availablePharmacies.add(p);
        }

        return availablePharmacies;
    }

    @Override
    @Transactional(readOnly = true)
    public List<DoctorMarkPharmaciesDTO> getFreeDoctorsForPharmacy(UUID pharmacyId, DateTimeRangeDTO timeRange) {
        List<Doctor> doctors = doctorRepository.findAllPharmacistsInPharmacy(pharmacyId);
        List<DoctorMarkPharmaciesDTO> freeDoctors = new ArrayList<>();

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

            DoctorMarkPharmaciesDTO doctorMarkPharmaciesDTO = new DoctorMarkPharmaciesDTO();
            doctorMarkPharmaciesDTO.setId(d.getId());
            doctorMarkPharmaciesDTO.setName(d.getName());
            doctorMarkPharmaciesDTO.setSurname(d.getSurname());
            float averageMark = markRepository.getAverageMarkForDoctor(d.getId());
            doctorMarkPharmaciesDTO.setAverageMark(averageMark);

            freeDoctors.add(doctorMarkPharmaciesDTO); // Ako nista od ovoga nije tacno, doktor je slobodan
        }

        return freeDoctors;
    }

    @Override
    @Lock(LockModeType.PESSIMISTIC_WRITE)   // Da ne bi slučajno zakazao dva termina u isto vrijeme
    @Transactional(rollbackFor = {AlreadyScheduledException.class, ScheduleTermException.class, RuntimeException.class})
    public boolean patientScheduleCounseling(ScheduleCounselingDTO term) throws AlreadyScheduledException, ScheduleTermException, RuntimeException {
        Patient patient = patientRepository.findById(term.getPatientId()).orElse(null);
        if (patient == null) throw new ScheduleTermException("");
        if (patient.getPenalties() >= 3) throw new ScheduleTermException("You cannot schedule terms");

        if (!termService.isPatientFree(term.getPatientId(), term.getFromTime(), term.getToTime()))
            throw new AlreadyScheduledException("You are busy at this time");

        Term checkTerm = counselingRepository.checkIfPatientHasCounselingWithDoctor(term.getPatientId(), term.getDoctorId(), new Date());
        if (checkTerm != null)
            throw new AlreadyScheduledException("You already have a counseling with this pharmacist.");

        UUID id = UUID.randomUUID();
        Doctor doctor = new Doctor();
        doctor.setId(term.getDoctorId());
        Term counseling = new Term(id, patient, doctor, term.getFromTime(), term.getToTime(), 10, TermType.counseling, null, false, 0L);
        counseling = counselingRepository.save(counseling);

        String userMail = patient.getEmail();
        String subject = "[ISA Pharmacy] Confirmation - Counseling scheduling";
        String body = "You have successfully scheduled a counseling on our site.\n" +
                "Your reservation ID: " + counseling.getId().toString();
        new Thread(() -> emailService.sendMail(userMail, subject, body)).start();

        return true;
    }

    @Override
    @Transactional(rollbackFor = {EntityNotFoundException.class, ActionNotAllowedException.class, RuntimeException.class})
    public boolean patientCancelCounseling(CancelTermDTO term) throws EntityNotFoundException, ActionNotAllowedException, RuntimeException {
        Term checkTerm = counselingRepository.findByIdCustom(term.getTermId());

        if (checkTerm == null) throw new EntityNotFoundException("There is no such counseling");
        if (!checkTerm.getPatient().getId().equals(term.getPatientId()))
            throw new ActionNotAllowedException("You can only cancel your own counselings");

        Date now = new Date(System.currentTimeMillis());
        if (checkTerm.getStartTime().before(now))
            throw new ActionNotAllowedException("Cannot cancel any past counselings");

        Date termTime = new Date(checkTerm.getStartTime().getTime());
        int diffInHours = (int) (termTime.getTime() - now.getTime()) / (1000 * 60 * 60);
        if (diffInHours <= 23) throw new ActionNotAllowedException("Cannot cancel 24hrs before the counseling");

        int rowsUpdated = counselingRepository.patientCancelCounseling(term.getTermId());
        if (rowsUpdated != 1) throw new RuntimeException("Couldn't cancel this term!");

        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TermDTO> findAllPatientsCounselings(UUID id) {
        List<Term> counselings = counselingRepository.findAllPatientsCounselings(id, TermType.counseling);

        List<TermDTO> finalCounselings = new ArrayList<>();
        for (Term t : counselings) {
            Doctor doctor = doctorRepository.findDoctorByTermId(t.getId());
            DoctorDTO doctorDto = new DoctorDTO(doctor.getId(), doctor.getName(), doctor.getSurname(), doctor.getEmail(), doctor.getPhoneNumber(),
                    doctor.getRole(), null);
            TermDTO termDto = new TermDTO(t.getId(), t.getStartTime(), t.getEndTime(), t.getPrice(), t.getType(), doctorDto);
            finalCounselings.add(termDto);
        }

        return finalCounselings;
    }
}
