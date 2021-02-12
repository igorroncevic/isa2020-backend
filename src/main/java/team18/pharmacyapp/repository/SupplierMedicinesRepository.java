package team18.pharmacyapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import team18.pharmacyapp.model.medicine.SupplierMedicine;

import java.util.List;
import java.util.UUID;

public interface SupplierMedicinesRepository extends JpaRepository<SupplierMedicine, UUID> {
    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "INSERT INTO supplier_medicine(quantity, medicine_id, supplier_id) " +
            "VALUES (:quantity, :medicineId, :supplierId) ")
    int addSupplierMedicine(@Param("supplierId") UUID supplierId, @Param("medicineId") UUID medicineId, @Param("quantity") int quantity);

    @Query("SELECT m FROM SupplierMedicine m WHERE m.medicine.name=:medicineName")
    SupplierMedicine findByName(@Param("medicineName") String medicineName);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE supplier_medicine  SET quantity = quantity + :quantityN " +
            "WHERE medicine_id = :medicineId")
    int updateMedicineQuantity(UUID medicineId, int quantityN);

}
