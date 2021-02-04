package team18.pharmacyapp.service.interfaces;

import team18.pharmacyapp.model.dtos.UpdateProfileDataDTO;
import team18.pharmacyapp.model.exceptions.ActionNotAllowedException;
import team18.pharmacyapp.model.exceptions.EntityNotFoundException;
import team18.pharmacyapp.model.medicine.Medicine;
import team18.pharmacyapp.model.users.Patient;
import java.util.List;
import java.util.UUID;

public interface PatientService {
    List<Patient> findAll();
    int addPenalty(UUID patientId);
    List<Medicine> getAlergicTo(UUID patientId);

    Patient getPatientProfileInfo(UUID id);

    boolean updatePatientProfileInfo(UpdateProfileDataDTO patient) throws ActionNotAllowedException, EntityNotFoundException, RuntimeException;

    Patient getById(UUID id);
}
