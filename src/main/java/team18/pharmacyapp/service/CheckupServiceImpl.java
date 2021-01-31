package team18.pharmacyapp.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team18.pharmacyapp.model.Term;
import team18.pharmacyapp.model.dtos.ScheduleCheckupDTO;
import team18.pharmacyapp.model.enums.TermType;
import team18.pharmacyapp.model.exceptions.ActionNotAllowedException;
import team18.pharmacyapp.model.exceptions.EntityNotFoundException;
import team18.pharmacyapp.model.exceptions.ReserveMedicineException;
import team18.pharmacyapp.model.exceptions.ScheduleTermException;
import team18.pharmacyapp.model.users.Patient;
import team18.pharmacyapp.repository.CheckupRepository;
import team18.pharmacyapp.repository.PatientRepository;
import team18.pharmacyapp.service.interfaces.CheckupService;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class CheckupServiceImpl implements CheckupService {
    private final CheckupRepository checkupRepository;
    private final PatientRepository patientRepository;

    public CheckupServiceImpl(CheckupRepository checkupRepository, PatientRepository patientRepository) {
        this.checkupRepository = checkupRepository;
        this.patientRepository = patientRepository;
    }

    public Term findOne(UUID id) {
        return checkupRepository.findById(id).orElse(null);
    }

    public List<Term> findAll(TermType termType) {
        return checkupRepository.findAll(termType);
    }

    public List<Term> findAllAvailableCheckups() {
        Date todaysDate = new Date(System.currentTimeMillis() + 60 * 60 * 1000);
        return checkupRepository.findAllAvailableCheckups(todaysDate, TermType.checkup);
    }

    @Override
    public List<Term> findAllPatientsCheckups(UUID patientId) {
        return checkupRepository.findAllPatientsCheckups(patientId, TermType.checkup);
    }

    public Term save(Term term) {
        return checkupRepository.save(term);
    }

    public void deleteById(UUID id) {
        checkupRepository.deleteById(id);
    }

    @Transactional(rollbackFor = {ActionNotAllowedException.class, RuntimeException.class, ReserveMedicineException.class})
    public boolean patientScheduleCheckup(ScheduleCheckupDTO term) throws ActionNotAllowedException, ScheduleTermException, RuntimeException {
        Patient patient = patientRepository.getOne(term.getPatientId());
        if (patient.getPenalties() >= 3) throw new ActionNotAllowedException("You are not allowed to schedule terms!");

        Term checkTerm = checkupRepository.findById(term.getCheckupId()).orElseGet(null);
        if (checkTerm == null) return false;

        Date today = new Date(System.currentTimeMillis() - 60 * 1000);
        if (checkTerm.getStartTime().before(today)) throw new ScheduleTermException("Can't schedule past terms!");

        int rowsUpdated = checkupRepository.patientScheduleCheckup(term.getPatientId(), term.getCheckupId());
        if (rowsUpdated != 1) throw new RuntimeException("Couldn't schedule this term!");

        return true;
    }

    @Transactional(rollbackFor = {EntityNotFoundException.class, ActionNotAllowedException.class, RuntimeException.class})
    public boolean patientCancelCheckup(ScheduleCheckupDTO term) throws EntityNotFoundException, ActionNotAllowedException, RuntimeException {
        Term checkTerm = checkupRepository.findByIdCustom(term.getCheckupId());

        if (checkTerm == null) throw new EntityNotFoundException("There is no such checkup");
        if(!checkTerm.getPatient().getId().equals(term.getPatientId())) throw new ActionNotAllowedException("You can only cancel your own checkups");

        Date yesterday = new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000);
        if (checkTerm.getStartTime().before(yesterday)) throw new ActionNotAllowedException("Cannot cancel 24hrs before the checkup or any past checkups");

        int rowsUpdated = checkupRepository.patientCancelCheckup(term.getCheckupId());
        if (rowsUpdated != 1) throw new RuntimeException("Couldn't cancel this term!");

        return true;
    }

}
