package team18.pharmacyapp.service.interfaces;

import team18.pharmacyapp.model.Term;
import team18.pharmacyapp.model.dtos.CancelTermDTO;
import team18.pharmacyapp.model.dtos.DoctorTermDTO;
import team18.pharmacyapp.model.dtos.ScheduleCheckupDTO;
import team18.pharmacyapp.model.dtos.TermDTO;
import team18.pharmacyapp.model.enums.TermType;
import team18.pharmacyapp.model.exceptions.ActionNotAllowedException;
import team18.pharmacyapp.model.exceptions.AlreadyScheduledException;
import team18.pharmacyapp.model.exceptions.EntityNotFoundException;
import team18.pharmacyapp.model.exceptions.ScheduleTermException;

import java.util.List;
import java.util.UUID;

public interface CheckupService {
    TermDTO findOne(UUID id);

    DoctorTermDTO findByIdFetchPatint(UUID id);

    List<TermDTO> findAll(TermType termType);

    List<TermDTO> findAllAvailableCheckups();

    List<TermDTO> findAllPatientsCheckups(UUID patientId);

    Term save(Term term);

    void deleteById(UUID id);

    boolean patientScheduleCheckup(ScheduleCheckupDTO term) throws ActionNotAllowedException, ScheduleTermException, RuntimeException, AlreadyScheduledException;


    List<DoctorTermDTO> doctorPharmacyFree(UUID doctorId, UUID pharmacyId);

    boolean patientCancelCheckup(CancelTermDTO term) throws EntityNotFoundException, ActionNotAllowedException, RuntimeException;
}
