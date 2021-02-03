package team18.pharmacyapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import team18.pharmacyapp.model.Pricings;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface PricingsRepository extends JpaRepository<Pricings, UUID> {

    @Query("SELECT p FROM pricings p " +
            "JOIN FETCH p.pharmacyMedicine pm " +
            "JOIN FETCH pm.medicine m " +
            "JOIN FETCH pm.pharmacy ph " +
            "WHERE ph.id = :pharmacyId AND p.startDate < :currentDate AND p.endDate > :currentDate")
    List<Pricings> getAllCurrentPricingsForPharmacy(@Param("pharmacyId") UUID pharmacyId, @Param("currentDate") Date currentDate);

    @Query("SELECT p FROM pricings p " +
            "JOIN FETCH p.pharmacyMedicine pm " +
            "JOIN FETCH pm.medicine m " +
            "JOIN FETCH pm.pharmacy ph " +
            "WHERE ph.id = :pharmacyId AND m.id = :medicineId")
    List<Pricings> getAllPricingsForMedicine(@Param("pharmacyId") UUID pharmacyId, @Param("medicineId") UUID medicineId);

}
