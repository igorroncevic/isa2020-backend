package team18.pharmacyapp.service;

import org.springframework.stereotype.Service;
import team18.pharmacyapp.model.Term;
import team18.pharmacyapp.model.dtos.ScheduleCheckupDTO;
import team18.pharmacyapp.model.enums.TermType;
import team18.pharmacyapp.repository.CheckupRepository;
import team18.pharmacyapp.service.interfaces.CheckupService;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class CheckupServiceImpl implements CheckupService {
    private final CheckupRepository checkupRepository;

    public CheckupServiceImpl(CheckupRepository checkupRepository) {
        this.checkupRepository = checkupRepository;
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

    public boolean patientScheduleCheckup(ScheduleCheckupDTO term) {
        Term checkTerm;
        try {
            checkTerm = checkupRepository.findById(term.getCheckupId()).orElseThrow(null);
        } catch (Exception e) {
            return false;
        }

        Date today = new Date(System.currentTimeMillis() - 60 * 1000);
        if (checkTerm.getStartTime().before(today)) {
            return false;
        }
        int rowsUpdated = checkupRepository.patientScheduleCheckup(term.getPatientId(), term.getCheckupId());
        return rowsUpdated == 1;
    }

    public boolean patientCancelCheckup(ScheduleCheckupDTO term) {
        Term checkTerm;
        try {
            checkTerm = checkupRepository.findById(term.getCheckupId()).orElseThrow(null);
        } catch (Exception e) {
            return false;
        }

        Date yesterday = new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000);
        if (checkTerm.getStartTime().before(yesterday)) {     // ponedeljak < (ponedeljak - 1)?
            return false;
        }

        int rowsUpdated = checkupRepository.patientCancelCheckup(term.getCheckupId());
        return rowsUpdated == 1;
    }

}
