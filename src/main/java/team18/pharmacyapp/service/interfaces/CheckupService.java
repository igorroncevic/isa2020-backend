package team18.pharmacyapp.service.interfaces;

import team18.pharmacyapp.model.Term;
import team18.pharmacyapp.model.dtos.ScheduleCheckupDTO;
import team18.pharmacyapp.model.enums.TermType;

import java.util.List;
import java.util.UUID;

public interface CheckupService {
    Term findOne(UUID id);

    List<Term> findAll(TermType termType);

    List<Term> findAllAvailableCheckups();

    List<Term> findAllPatientsCheckups(UUID patientId);

    Term save(Term term);

    void deleteById(UUID id);

    boolean patientScheduleCheckup(ScheduleCheckupDTO term);

    boolean patientCancelCheckup(ScheduleCheckupDTO term);

}
