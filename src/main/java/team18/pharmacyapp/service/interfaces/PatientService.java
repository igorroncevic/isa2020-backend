package team18.pharmacyapp.service.interfaces;

import team18.pharmacyapp.model.dtos.MedicineIdNameDTO;
import team18.pharmacyapp.model.dtos.PatientDTO;
import team18.pharmacyapp.model.dtos.UpdateProfileDataDTO;
import team18.pharmacyapp.model.exceptions.ActionNotAllowedException;
import team18.pharmacyapp.model.exceptions.EntityNotFoundException;
import team18.pharmacyapp.model.users.Patient;
import team18.pharmacyapp.model.users.RegisteredUser;

import java.util.List;
import java.util.UUID;

public interface PatientService {
    List<Patient> findAll();

    int addPenalty(UUID patientId);

    List<MedicineIdNameDTO> getAlergicTo(UUID patientId);

    PatientDTO getPatientProfileInfo(UUID id);

    boolean updatePatientProfileInfo(UpdateProfileDataDTO patient) throws ActionNotAllowedException, EntityNotFoundException, RuntimeException;

    Patient getById(UUID id);

    int getPatientPenalties(UUID id);

    Patient register(RegisteredUser user);

    boolean isActivated(UUID patientId);

    boolean activateAcc(UUID patientId);

    RegisteredUser updateUser(String name, String surname, String phone, String password, UUID id);
}
