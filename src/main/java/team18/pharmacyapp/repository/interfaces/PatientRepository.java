package team18.pharmacyapp.repository.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import team18.pharmacyapp.model.users.Patient;

import java.util.UUID;

public interface PatientRepository extends JpaRepository<Patient, UUID> {
}
