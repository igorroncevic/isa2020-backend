package team18.pharmacyapp.service.interfaces;

import team18.pharmacyapp.model.Term;
import team18.pharmacyapp.model.dtos.*;
import team18.pharmacyapp.model.enums.TermType;
import team18.pharmacyapp.model.exceptions.*;

import java.util.List;
import java.util.UUID;

public interface CheckupService {
    TermDTO findOne(UUID id);

    DoctorTermDTO findByIdFetchPatint(UUID id);

    List<TermDTO> findAll(TermType termType);

    List<TermDTO> findAllAvailableCheckups();

    List<TermDTO> findAllAvailableDermatologistsCheckups(UUID doctorId);

    List<TermDTO> findAllPatientsCheckups(UUID patientId);

    void deleteById(UUID id);

    void save(Term term);

    boolean patientScheduleCheckup(ScheduleCheckupDTO term) throws ActionNotAllowedException, ScheduleTermException, RuntimeException, AlreadyScheduledException;

    List<DoctorTermDTO> doctorPharmacyFree(UUID doctorId, UUID pharmacyId);

    boolean patientCancelCheckup(CancelTermDTO term) throws EntityNotFoundException, ActionNotAllowedException, RuntimeException;

    boolean addNewCheckup(NewCheckupDTO newCheckupDTO) throws FailedToSaveException;
}
