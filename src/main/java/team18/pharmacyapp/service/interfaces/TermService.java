package team18.pharmacyapp.service.interfaces;

import team18.pharmacyapp.model.Term;
import team18.pharmacyapp.model.dtos.ScheduleTermDTO;
import team18.pharmacyapp.model.enums.TermType;

import java.util.List;
import java.util.UUID;

public interface TermService {
    Term findOne(UUID id);

    List<Term> findAll();

    List<Term> findAllAvailableTerms();

    List<Term> findAllAvailableTermsByType(TermType type);

    Term save(Term term);

    void deleteById(UUID id);

    boolean patientScheduleCheckup(ScheduleTermDTO term);

}
