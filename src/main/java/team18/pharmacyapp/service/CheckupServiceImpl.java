package team18.pharmacyapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team18.pharmacyapp.model.Term;
import team18.pharmacyapp.model.dtos.*;
import team18.pharmacyapp.model.enums.TermType;
import team18.pharmacyapp.model.exceptions.ActionNotAllowedException;
import team18.pharmacyapp.model.exceptions.AlreadyScheduledException;
import team18.pharmacyapp.model.exceptions.EntityNotFoundException;
import team18.pharmacyapp.model.exceptions.ScheduleTermException;
import team18.pharmacyapp.model.users.Doctor;
import team18.pharmacyapp.model.users.Patient;
import team18.pharmacyapp.repository.CheckupRepository;
import team18.pharmacyapp.repository.TermRepository;
import team18.pharmacyapp.repository.users.DoctorRepository;
import team18.pharmacyapp.repository.users.PatientRepository;
import team18.pharmacyapp.service.interfaces.CheckupService;
import team18.pharmacyapp.service.interfaces.EmailService;
import team18.pharmacyapp.service.interfaces.TermService;

import javax.persistence.LockModeType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class CheckupServiceImpl implements CheckupService {
    private final CheckupRepository checkupRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final TermService termService;
    private final TermRepository termRepository;
    private final EmailService emailService;

    @Autowired
    public CheckupServiceImpl(CheckupRepository checkupRepository, PatientRepository patientRepository, DoctorRepository doctorRepository, TermService termService, TermRepository termRepository, EmailService emailService) {
        this.checkupRepository = checkupRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.termService = termService;
        this.termRepository = termRepository;
        this.emailService = emailService;
    }

    @Override
    public TermDTO findOne(UUID id) {
        Term t = checkupRepository.findById(id).orElse(null);
        TermDTO termDto = new TermDTO(t.getId(), t.getStartTime(), t.getEndTime(), t.getPrice(), t.getType(), null);

        return termDto;
    }

    @Override
    public DoctorTermDTO findByIdFetchPatint(UUID id) {
        Term t = checkupRepository.findByIdCustom(id);
        Patient p = t.getPatient();
        return new DoctorTermDTO(t.getId(), t.getStartTime(), t.getEndTime(), t.getType(), new DoctorsPatientDTO(p.getId(), p.getName(), p.getSurname(), p.getEmail(), p.getPhoneNumber()));
    }

    @Override
    @Transactional(readOnly = true)
    public List<TermDTO> findAll(TermType termType) {
        List<Term> checkups = checkupRepository.findAll(TermType.checkup);

        List<TermDTO> finalCheckups = new ArrayList<>();
        for (Term t : checkups) {
            Doctor doctor = doctorRepository.findDoctorByTermId(t.getId());
            DoctorDTO doctorDto = new DoctorDTO(doctor.getId(), doctor.getName(), doctor.getSurname(), doctor.getEmail(), doctor.getPhoneNumber(),
                    doctor.getRole(), null);
            TermDTO termDto = new TermDTO(t.getId(), t.getStartTime(), t.getEndTime(), t.getPrice(), t.getType(), doctorDto);
            finalCheckups.add(termDto);
        }

        return finalCheckups;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TermDTO> findAllAvailableCheckups() {
        Date todaysDate = new Date(System.currentTimeMillis() + 60 * 60 * 1000);

        List<Term> checkups = checkupRepository.findAllAvailableCheckups(todaysDate, TermType.checkup);

        List<TermDTO> finalCheckups = new ArrayList<>();
        for (Term t : checkups) {
            Doctor doctor = doctorRepository.findDoctorByTermId(t.getId());
            DoctorDTO doctorDto = new DoctorDTO(doctor.getId(), doctor.getName(), doctor.getSurname(), doctor.getEmail(), doctor.getPhoneNumber(),
                    doctor.getRole(), null);
            TermDTO termDto = new TermDTO(t.getId(), t.getStartTime(), t.getEndTime(), t.getPrice(), t.getType(), doctorDto);
            finalCheckups.add(termDto);
        }

        return finalCheckups;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TermDTO> findAllPatientsCheckups(UUID patientId) {
        List<Term> checkups = checkupRepository.findAllPatientsCheckups(patientId, TermType.checkup);

        List<TermDTO> finalCheckups = new ArrayList<>();
        for (Term t : checkups) {
            Doctor doctor = doctorRepository.findDoctorByTermId(t.getId());
            DoctorDTO doctorDto = new DoctorDTO(doctor.getId(), doctor.getName(), doctor.getSurname(), doctor.getEmail(), doctor.getPhoneNumber(),
                    doctor.getRole(), null);
            TermDTO termDto = new TermDTO(t.getId(), t.getStartTime(), t.getEndTime(), t.getPrice(), t.getType(), doctorDto);
            finalCheckups.add(termDto);
        }

        return finalCheckups;
    }

    public Term save(Term term) {
        return checkupRepository.save(term);
    }

    public void deleteById(UUID id) {
        checkupRepository.deleteById(id);
    }

    @Override
    @Lock(LockModeType.WRITE)
    @Transactional(rollbackFor = {ActionNotAllowedException.class, AlreadyScheduledException.class, RuntimeException.class, ScheduleTermException.class})
    public boolean patientScheduleCheckup(ScheduleCheckupDTO term) throws ActionNotAllowedException, ScheduleTermException, RuntimeException, AlreadyScheduledException {
        Patient patient = patientRepository.getOne(term.getPatientId());
        if (patient.getPenalties() >= 3) throw new ActionNotAllowedException("You are not allowed to schedule terms!");

        Term checkTerm = checkupRepository.findById(term.getCheckupId()).orElseGet(null);
        if (checkTerm == null) return false;

        if (!termService.isPatientFree(term.getPatientId(), checkTerm.getStartTime(), checkTerm.getEndTime()))
            throw new AlreadyScheduledException("You are busy at this time");

        Date today = new Date(System.currentTimeMillis());
        if (checkTerm.getStartTime().before(today)) throw new ScheduleTermException("Can't schedule past terms!");

        checkTerm.setPatient(patient);
        checkupRepository.save(checkTerm);

        String userMail = patient.getEmail();   // zakucano za sada
        String subject = "[ISA Pharmacy] Confirmation - Checkup scheduling";
        String body = "You have successfully scheduled a checkup on our site.\n" +
                "Your reservation ID: " + checkTerm.getId().toString();
        new Thread(() -> emailService.sendMail(userMail, subject, body)).start();

        return true;
    }

    @Override
    @Transactional(rollbackFor = {EntityNotFoundException.class, ActionNotAllowedException.class, RuntimeException.class})
    public boolean patientCancelCheckup(CancelTermDTO term) throws EntityNotFoundException, ActionNotAllowedException, RuntimeException {
        Term checkTerm = checkupRepository.findByIdCustom(term.getTermId());

        if (checkTerm == null) throw new EntityNotFoundException("There is no such checkup");
        if (!checkTerm.getPatient().getId().equals(term.getPatientId()))
            throw new ActionNotAllowedException("You can only cancel your own checkups");

        Date now = new Date(System.currentTimeMillis());
        int diffInHours = (int) ((checkTerm.getStartTime().getTime() - (now.getTime()) / (1000 * 60 * 60)));

        if (diffInHours >= 0 && diffInHours <= 23)
            throw new ActionNotAllowedException("Cannot cancel 24hrs before the checkup");
        if (checkTerm.getStartTime().before(now))
            throw new ActionNotAllowedException("Cannot cancel any past checkups");

        // Zaključavanje nije potrebno jer samo pacijent može da pristupi tom resursu
        int rowsUpdated = checkupRepository.patientCancelCheckup(term.getTermId());
        if (rowsUpdated != 1) throw new RuntimeException("Couldn't cancel this term!");

        return true;
    }

    @Override
    public List<DoctorTermDTO> doctorPharmacyFree(UUID doctorId, UUID pharmacyId) {
        List<DoctorTermDTO> list = new ArrayList<>();
        for (Term term : termRepository.findAllFreeTermsForDoctorInPharmacy(doctorId, pharmacyId)) {
            list.add(new DoctorTermDTO(term.getId(), term.getStartTime(), term.getEndTime(), term.getType(), null));
        }
        return list;
    }


}
