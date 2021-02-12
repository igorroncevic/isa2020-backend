package team18.pharmacyapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import team18.pharmacyapp.model.dtos.MedicineAllergyDTO;
import team18.pharmacyapp.model.medicine.Medicine;
import team18.pharmacyapp.model.medicine.MedicineSpecification;
import team18.pharmacyapp.model.medicine.PharmacyMedicines;
import team18.pharmacyapp.model.medicine.ReservedMedicines;
import team18.pharmacyapp.model.medicine.SupplierMedicine;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface MedicineRepository extends JpaRepository<Medicine, UUID> {
    @Transactional(readOnly = true)
    @Query(value = "SELECT distinct p FROM medicine m INNER JOIN pharmacy_medicines p ON p.medicine = m.id JOIN FETCH p.pricings " +
            "JOIN FETCH p.pharmacy JOIN FETCH p.medicine WHERE p.quantity > 0 ORDER BY p.quantity DESC")
    List<PharmacyMedicines> findAllAvailableMedicines();

    @Transactional(readOnly = true)
    @Query(value = "SELECT distinct m FROM medicine m join fetch m.marks JOIN m.pharmacyMedicines pm WHERE pm.quantity > 0 ")
    List<Medicine> findAllAvailableMedicinesWithMarksNoAuth();

    @Transactional(readOnly = true)
    @Query(value = "SELECT distinct m FROM medicine m JOIN m.pharmacyMedicines pm WHERE pm.quantity > 0 and m.marks is empty ")
    List<Medicine> findAllAvailableMedicinesNoMarksNoAuth();

    @Transactional(readOnly = true)
    @Query("SELECT r FROM reserved_medicines r JOIN FETCH r.patient WHERE r.pickupDate > :todaysDate AND r.handled = false")
    List<ReservedMedicines> findAllNonHandledReservedMedicines(@Param("todaysDate") Date todaysDate);

    @Transactional(readOnly = true)
    @Query("SELECT rm FROM reserved_medicines rm " +
            "JOIN FETCH rm.medicine JOIN FETCH rm.pharmacy " +
            "WHERE rm.patient.id = :patientId AND rm.handled = false")
    List<ReservedMedicines> findAllPatientsReservedMedicinesNotPickedUp(@Param("patientId") UUID patientId);

    @Transactional(readOnly = true)
    @Query("SELECT m FROM medicine m " +
            "WHERE m.id IN " +
            "(SELECT m.id FROM medicine m JOIN m.reservedMedicines rm WHERE rm.patient.id = :patientId AND rm.handled = true) " +
            "OR m.id IN " +
            "(SELECT m.id FROM medicine m JOIN m.pharmacyMedicines pm JOIN pm.ePrescriptionMedicines epm JOIN epm.ePrescription ep " +
            "WHERE ep.patient.id = :patientId)")
    List<Medicine> getPatientsMedicines(@Param("patientId") UUID patientId);

    @Transactional(readOnly = true)
    @Query(nativeQuery = true, value = "SELECT pickup_date FROM reserved_medicines WHERE id = :id")
    Date findPickupDateByReservationId(@Param("id") UUID id);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "INSERT INTO reserved_medicines(id, patient_id, pharmacy_id, medicine_id, pickup_date, handled) " +
            "VALUES (:id, :patientId, :pharmacyId, :medicineId, :pickupDate, false) ")
    int reserveMedicine(@Param("id") UUID id, @Param("patientId") UUID patientId, @Param("pharmacyId") UUID pharmacyId, @Param("medicineId") UUID medicineId, @Param("pickupDate") Date pickupDate);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE pharmacy_medicines SET quantity = quantity - 1 " +
            "WHERE pharmacy_id = :pharmacyId AND medicine_id = :medicineId AND quantity > 0")
    int decrementMedicineQuantity(@Param("medicineId") UUID medicineId, @Param("pharmacyId") UUID pharmacyId);

    @Transactional
    @Modifying
    @Query("DELETE FROM reserved_medicines WHERE id = :id")
    int cancelMedicine(@Param("id") UUID id);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE pharmacy_medicines SET quantity = quantity + 1 " +
            "WHERE pharmacy_id = :pharmacyId AND medicine_id = :medicineId")
    int incrementMedicineQuantity(@Param("medicineId") UUID medicineId, @Param("pharmacyId") UUID pharmacyId);

    @Transactional(readOnly = true)
    @Query("SELECT m FROM medicine m " +
            "JOIN m.reservedMedicines rm " +
            "WHERE rm.patient.id = :patientId AND rm.medicine.id = :medicineId AND rm.handled = true")
    Medicine checkIfPatientReservedMedicine(@Param("medicineId")UUID medicineId, @Param("patientId") UUID patientId);

    @Transactional(readOnly = true)
    @Query("SELECT m FROM medicine m " +
            "JOIN FETCH m.pharmacyMedicines pm " +
            "JOIN pm.ePrescriptionMedicines epm " +
            "JOIN epm.ePrescription e " +
            "WHERE e.patient.id = :patientId AND pm.medicine.id = :medicineId")
    Medicine checkIfPatientGotPrescribedMedicine(@Param("medicineId")UUID medicineId, @Param("patientId") UUID patientId);

    @Transactional(readOnly = true)
    @Query("SELECT m FROM medicine m " +
            "JOIN FETCH m.pharmacyMedicines pm " +
            "JOIN pm.ePrescriptionMedicines epm " +
            "JOIN epm.ePrescription e " +
            "WHERE pm.pharmacy.id = :pharmacyId AND e.patient.id = :patientId")
    List<Medicine> getPatientsEPrescriptionMedicinesFromPharmacy(@Param("pharmacyId")UUID pharmacyId, @Param("patientId") UUID patientId);

    @Transactional(readOnly = true)
    @Query("SELECT m FROM medicine m " +
            "JOIN m.reservedMedicines rm " +
            "WHERE rm.patient.id = :patientId AND rm.pharmacy.id = :pharmacyId AND rm.handled = true")
    List<Medicine> getPatientsReservedMedicinesFromPharmacy(@Param("pharmacyId")UUID pharmacyId, @Param("patientId") UUID patientId);

    @Transactional(readOnly = true)
    @Query(nativeQuery = true, value = "SELECT * FROM medicine m INNER JOIN alergicto a on m.id = a.medicine_id WHERE a.patient_id = :patientId")
    List<Medicine>getMedicinesPatientsAllergicTo(@Param("patientId")UUID patientId);

    @Transactional(readOnly = true)
    @Query(nativeQuery = true, value = "SELECT * FROM medicine m WHERE m.id " +
            "NOT IN (SELECT m2.id FROM medicine m2 INNER JOIN alergicto a on m2.id = a.medicine_id WHERE a.patient_id = :patientId)")
    List<Medicine> getAllMedicinesPatientsNotAlergicTo(@Param("patientId") UUID patientId);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "INSERT INTO alergicto(patient_id, medicine_id) VALUES (:patientId, :medicineId)")
    int addNewAllergy(@Param("patientId") UUID patientId, @Param("medicineId") UUID medicineId);

    @Query("SELECT sm FROM SupplierMedicine sm WHERE sm.supplier.id=:supplierId")
    List<SupplierMedicine> findMedicinesBySupplierId(UUID supplierId);

    @Query("SELECT m FROM medicine m WHERE m.name=:medicineName")
    Medicine findByName(@Param("medicineName") String medicineName);

    @Transactional(readOnly = true)
    @Query(value = "select s from medicine_specification s where s.medicine.id=:medicineId ")
    MedicineSpecification getMedicineSpecification (UUID medicineId);

    @Transactional(readOnly = true)
    @Query(value = "select m.name from medicine_specification s inner join medicine m on s.replacementMedicineCode=m.medicineCode where s.medicine.id=:medicineId ")
    String getReplacmentMedicine (UUID medicineId);
}
