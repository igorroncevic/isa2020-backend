package team18.pharmacyapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import team18.pharmacyapp.model.Promotion;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface PromotionRepository extends JpaRepository<Promotion, UUID> {

    @Query(nativeQuery = true, value = "SELECT * FROM promotion WHERE pharmacy_id=:id")
    List<Promotion> getAllForPharmacy(UUID id);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "INSERT INTO promotion(id, start_date, end_date, text, pharmacy_id) VALUES (:id, :startDate, :endDate, :text, :pharmacyId)")
    int insert(UUID id, Date startDate, Date endDate, String text, UUID pharmacyId);

    @Query(nativeQuery = true, value = "SELECT p.email FROM pharmacy_subscribed_patients psp JOIN patient p ON p.id = psp.subscribed_patients_id " +
            "WHERE psp.subscribed_pharmacies_id = :pharmacyId")
    List<String> getEmailsOfSubscribedPatients(UUID pharmacyId);

    @Transactional(readOnly = true)
    @Query("SELECT p FROM promotion p JOIN FETCH p.pharmacy ph JOIN FETCH ph.address JOIN ph.subscribedPatients sp WHERE sp.id = :patientId")
    List<Promotion> getPatientsPromotions(@Param("patientId") UUID patientId);
}
