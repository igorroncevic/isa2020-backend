package team18.pharmacyapp.service.interfaces;

import team18.pharmacyapp.model.users.Patient;
import java.util.List;
import java.util.UUID;

public interface PatientService {
    List<Patient> findAll();
    int addPenalty(UUID patientId);
}
