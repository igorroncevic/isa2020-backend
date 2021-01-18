package team18.pharmacyapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import team18.pharmacyapp.model.dtos.ReservedMedicineDTO;
import team18.pharmacyapp.model.medicine.PharmacyMedicines;
import team18.pharmacyapp.model.medicine.Medicine;
import team18.pharmacyapp.model.users.Patient;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface MedicineRepository extends JpaRepository<Medicine, UUID> {
    @Transactional(readOnly = true)
    @Query(value = "SELECT p FROM medicine m INNER JOIN pharmacy_medicines p ON p.medicine = m.id JOIN FETCH p.pricings " +
            "JOIN FETCH p.pharmacy JOIN FETCH p.medicine WHERE p.quantity > 0 ORDER BY p.quantity DESC")
    List<PharmacyMedicines> findAllAvailableMedicines();

    @Transactional(readOnly = true)
    @Query(value = "SELECT r, p FROM reserved_medicines r INNER JOIN pharmacy_medicines p ON p.medicine = r.medicine " +
            "JOIN FETCH r.medicine JOIN FETCH p.pricings JOIN FETCH p.pharmacy WHERE r.patient = :patient")
    List<ReservedMedicineDTO> findAllPatientsReservedMedicines(@Param("patient") Patient patient);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "INSERT INTO reserved_medicines(patient_id, pharmacy_id, medicine_id, pickup_date) VALUES (:patientId, :pharmacyId, :medicineId, :pickupDate) ")
    int reserveMedicine(@Param("patientId") UUID patientId, @Param("pharmacyId")  UUID pharmacyId, @Param("medicineId") UUID medicineId, @Param("pickupDate") Date pickupDate);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE pharmacy_medicines SET quantity = quantity - 1 " +
            "WHERE pharmacy_id = :pharmacyId AND medicine_id = :medicineId AND quantity > 0")
    int updateMedicineQuantity(@Param("medicineId") UUID medicineId, @Param("pharmacyId") UUID pharmacyId);
}
