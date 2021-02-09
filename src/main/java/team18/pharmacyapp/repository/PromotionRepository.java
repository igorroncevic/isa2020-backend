package team18.pharmacyapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import team18.pharmacyapp.model.Promotion;

import java.util.List;
import java.util.UUID;

public interface PromotionRepository extends JpaRepository<Promotion, UUID> {

    @Query(nativeQuery = true, value = "SELECT * FROM promotion WHERE pharmacy_id=:id")
    List<Promotion> getAllForPharmacy(UUID id);

}
