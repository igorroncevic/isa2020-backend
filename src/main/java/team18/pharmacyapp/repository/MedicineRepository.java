package team18.pharmacyapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import team18.pharmacyapp.model.medicine.PharmacyMedicines;
import team18.pharmacyapp.model.medicine.Medicine;

import java.util.List;
import java.util.UUID;

public interface MedicineRepository extends JpaRepository<Medicine, UUID> {
    @Transactional(readOnly = true)
    @Query(value = "SELECT p FROM medicine m INNER JOIN pharmacy_medicines p ON p.medicine = m.id JOIN FETCH p.pricings " +
            "JOIN FETCH p.pharmacy JOIN FETCH p.medicine WHERE p.quantity > 0 ORDER BY p.quantity DESC")
    List<PharmacyMedicines> findAllAvailableMedicines();
}
