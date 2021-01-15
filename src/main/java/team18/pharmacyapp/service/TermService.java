package team18.pharmacyapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team18.pharmacyapp.model.Term;
import team18.pharmacyapp.model.dtos.TermDTO;
import team18.pharmacyapp.repository.TermRepository;

import java.util.List;
import java.util.UUID;

@Service
public class TermService {
    @Autowired
    private TermRepository termRepository;

    public Term findOne(UUID id){
        return termRepository.findById(id).orElse(null);
    }

    public List<Term>findAll(){
        return termRepository.findAllCustom();
    }

    public Term save(Term term){
        return termRepository.save(term);
    }

    public void deleteById(UUID id){
        termRepository.deleteById(id);
    }

    public boolean patientScheduleCheckup(TermDTO term){
        int rowsUpdated = termRepository.patientScheduleCheckup(term.getPatient_id(), term.getId());
        return rowsUpdated == 1;
    }

}
