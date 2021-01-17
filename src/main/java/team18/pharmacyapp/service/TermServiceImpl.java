package team18.pharmacyapp.service;

import org.springframework.stereotype.Service;
import team18.pharmacyapp.model.Term;
import team18.pharmacyapp.model.dtos.ScheduleTermDTO;
import team18.pharmacyapp.model.enums.TermType;
import team18.pharmacyapp.repository.TermRepository;
import team18.pharmacyapp.service.interfaces.TermService;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class TermServiceImpl implements TermService {
    private final TermRepository termRepository;

    public TermServiceImpl(TermRepository termRepository) {
        this.termRepository = termRepository;
    }

    public Term findOne(UUID id) {
        return termRepository.findById(id).orElse(null);
    }

    public List<Term> findAll() {
        return termRepository.findAll();
    }

    public List<Term> findAllAvailableTermsByType(TermType type){
        Date today = new Date(System.currentTimeMillis() + 60 * 60 * 1000); // termini 1h od ovog trenutka
        return termRepository.findAllAvailableTermsByType(type, today);
    }

    public List<Term> findAllAvailableTerms(){
        Date todaysDate = new Date(System.currentTimeMillis() + 60 * 60 * 1000);
        return termRepository.findAllAvailableTerms(todaysDate);
    }

    public Term save(Term term) {
        return termRepository.save(term);
    }

    public void deleteById(UUID id) {
        termRepository.deleteById(id);
    }

    public boolean patientScheduleCheckup(ScheduleTermDTO term) {
        Term checkTerm = termRepository.findById(term.getTermId()).orElse(null);
        Date today = new Date(System.currentTimeMillis() - 60 * 1000);
        if(checkTerm.getStartTime().before(today)){
            return false;
        }
        int rowsUpdated = termRepository.patientScheduleCheckup(term.getPatientId(), term.getTermId());
        return rowsUpdated == 1;
    }

    public boolean patientCancelCheckup(ScheduleTermDTO term) {
        Term checkTerm = termRepository.findById(term.getTermId()).orElse(null);
        Date yesterday = new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000);
        if(checkTerm.getStartTime().before(yesterday)){     // ponedeljak < (ponedeljak - 1)?
            return false;
        }

        int rowsUpdated = termRepository.patientCancelCheckup(term.getTermId());
        return rowsUpdated == 1;
    }

}
