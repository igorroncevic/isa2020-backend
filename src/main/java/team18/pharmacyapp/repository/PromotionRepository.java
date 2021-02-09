package team18.pharmacyapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import team18.pharmacyapp.model.Promotion;

import java.util.List;
import java.util.UUID;

public interface PromotionRepository extends JpaRepository<Promotion, UUID> {

    @Transactional(readOnly = true)
    @Query("SELECT p FROM Promotion p JOIN FETCH p.pharmacy ph JOIN FETCH ph.address JOIN ph.subscribedPatients sp WHERE sp.id = :patientId")
    List<Promotion> getPatientsPromotions(@Param("patientId") UUID patientId);
}
