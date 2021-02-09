package team18.pharmacyapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import team18.pharmacyapp.model.Promotion;

import javax.transaction.Transactional;
import java.time.LocalDate;
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

}
