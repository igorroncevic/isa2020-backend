package team18.pharmacyapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.transaction.annotation.Transactional;
import team18.pharmacyapp.model.Loyalty;

import java.util.UUID;

public interface LoyaltyRepository extends JpaRepository<Loyalty, UUID> {
    Loyalty findByCategory(String category);

    @Transactional(readOnly = true)
    @Query("SELECT l FROM Loyalty l JOIN l.patients p WHERE p.id = :patientId")
    Loyalty getPatientsLoyalty(@Param("patientId") UUID patientId);
}
