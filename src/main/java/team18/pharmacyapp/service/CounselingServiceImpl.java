package team18.pharmacyapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
import team18.pharmacyapp.repository.*;
import team18.pharmacyapp.service.interfaces.CounselingService;
import team18.pharmacyapp.service.interfaces.EmailService;
import team18.pharmacyapp.service.interfaces.TermService;

import java.time.LocalTime;
import java.util.*;

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
            float averageMark = markRepository.getAverageMarkForDoctor(d.getId());
            doctorDTO.setAverageMark(averageMark);

            freeDoctors.add(doctorDTO); // Ako nista od ovoga nije tacno, doktor je slobodan
        }

        return freeDoctors;
    }

    @Override
    @Transactional(rollbackFor = {AlreadyScheduledException.class, ScheduleTermException.class, RuntimeException.class})
    public boolean patientScheduleCounseling(ScheduleCounselingDTO term) throws AlreadyScheduledException, ScheduleTermException, RuntimeException {
        Patient patient = patientRepository.findById(term.getPatientId()).orElse(null);
        if(patient == null) throw new ScheduleTermException("");
        if(patient.getPenalties() >= 3) throw new ScheduleTermException("You cannot schedule terms");

        if(!termService.isPatientFree(term.getPatientId(), term.getFromTime(), term.getToTime())) throw new AlreadyScheduledException("You are busy at this time");

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

    @Override
    public List<Term> findAllPatientsCounselings(UUID id) {
        return counselingRepository.findAllPatientsCounselings(id, TermType.counseling);
    }

    @Override
    public TermPaginationDTO findAllPatientsPastCounselingsPaginated(UUID id, String sort, int page) {
        String[] sortParts = sort.split(" ");
        int startPage = page - 1; // Jer krecu od 1, a ako hocemo prvi da prikazemo, Pageable krece od 0

        Pageable pageable;
        if(sortParts[1].equalsIgnoreCase("asc.")){
            pageable = PageRequest.of(startPage, 3, Sort.by(sortParts[0]).ascending());  // Zakucano 3 po stranici
        }else {
            pageable = PageRequest.of(startPage, 3, Sort.by(sortParts[0]).descending());
        }
        Page<Term> allCounselings = counselingRepository.findAllByPatient_IdAndType(id, TermType.counseling, pageable);

        TermPaginationDTO response = new TermPaginationDTO();
        if(!allCounselings.hasContent()) return response;

        List<Term> pastCounselings = new ArrayList<>();
        Date today = new Date(System.currentTimeMillis());
        for(Term t : allCounselings.getContent()){
            if(t.getStartTime().before(today)){
                pastCounselings.add(t);
            }
        }

        response.setTerms(pastCounselings);
        response.setTotalPages(allCounselings.getTotalPages());

        return response;
    }

}
