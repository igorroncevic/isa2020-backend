package team18.pharmacyapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import team18.pharmacyapp.model.users.Patient;

import java.util.UUID;

public interface PatientRepository extends JpaRepository<Patient, UUID> {

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE patient SET penalties = penalties + 1 " +
            "WHERE id = :patientId AND penalties < 3")
    int addPenalty(@Param("patientId") UUID patientId);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE patient SET penalties = 0 WHERE id = :patientId")
    int resetPenalties(@Param("patientId") UUID patientId);

    Patient findByEmailAndPassword(String email, String password);
    Patient findByEmail(String email);
}
