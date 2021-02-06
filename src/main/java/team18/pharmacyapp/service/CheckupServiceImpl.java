package team18.pharmacyapp.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team18.pharmacyapp.model.Term;
import team18.pharmacyapp.model.dtos.ScheduleCheckupDTO;
import team18.pharmacyapp.model.dtos.TermPaginationDTO;
import team18.pharmacyapp.model.enums.TermType;
import team18.pharmacyapp.model.exceptions.*;
import team18.pharmacyapp.model.users.Patient;
import team18.pharmacyapp.repository.CheckupRepository;
import team18.pharmacyapp.repository.PatientRepository;
import team18.pharmacyapp.service.interfaces.CheckupService;
import team18.pharmacyapp.service.interfaces.TermService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class CheckupServiceImpl implements CheckupService {
    private final CheckupRepository checkupRepository;
    private final PatientRepository patientRepository;
    private final TermService termService;

    public CheckupServiceImpl(CheckupRepository checkupRepository, PatientRepository patientRepository, TermService termService) {
        this.checkupRepository = checkupRepository;
        this.patientRepository = patientRepository;
        this.termService = termService;
    }

    public Term findOne(UUID id) {
        return checkupRepository.findById(id).orElse(null);
    }

    @Override
    public Term findByIdFetchDoctor(UUID id) {
        return checkupRepository.findByIdCustom(id);
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

    @Transactional(rollbackFor = {ActionNotAllowedException.class, AlreadyScheduledException.class, RuntimeException.class, ScheduleTermException.class})
    public boolean patientScheduleCheckup(ScheduleCheckupDTO term) throws ActionNotAllowedException, ScheduleTermException, RuntimeException, AlreadyScheduledException {
        Patient patient = patientRepository.getOne(term.getPatientId());
        if (patient.getPenalties() >= 3) throw new ActionNotAllowedException("You are not allowed to schedule terms!");

        Term checkTerm = checkupRepository.findById(term.getCheckupId()).orElseGet(null);
        if (checkTerm == null) return false;

        if(!termService.isPatientFree(term.getPatientId(), checkTerm.getStartTime(), checkTerm.getEndTime())) throw new AlreadyScheduledException("You are busy at this time");

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
