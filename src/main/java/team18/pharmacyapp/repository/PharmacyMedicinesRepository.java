package team18.pharmacyapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import team18.pharmacyapp.model.dtos.ReservedMedicineDTO;
import team18.pharmacyapp.model.keys.PharmacyMedicinesId;
import team18.pharmacyapp.model.medicine.Medicine;
import team18.pharmacyapp.model.medicine.PharmacyMedicines;
import team18.pharmacyapp.model.users.Patient;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface PharmacyMedicinesRepository extends JpaRepository<PharmacyMedicines, PharmacyMedicinesId> {
    @Transactional(readOnly = true)
    @Query(value = "select quantity from pharmacy_medicines where pharmacy_id=:pharmacyId and medicine_id=:medicineId",nativeQuery = true)
    int findQyantity(UUID pharmacyId,UUID medicineId);

    @Transactional(readOnly = true)
    @Query(value = "select p, m from pharmacy_medicines p inner join medicine m on p.medicine.id=m.id where p.pharmacy.id=:pharmacyId")
    List<PharmacyMedicines> getMedicineByPharmacy (UUID pharmacyId);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "INSERT INTO pharmacy_medicines (pharmacy_id, medicine_id, quantity, version) VALUES (:pharmacyId, :medicineId, 0, 1)")
    void insert(UUID pharmacyId, UUID medicineId);

    @Transactional(readOnly = true)
    @Query(nativeQuery = true, value = "SELECT COUNT(*) FROM reserved_medicines WHERE pharmacy_id = :pharmacyId and medicine_id = :medicineId and handled = false")
    int getNumberOfUnhandledReservations(UUID pharmacyId, UUID medicineId);
}
