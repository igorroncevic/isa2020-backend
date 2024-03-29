package team18.pharmacyapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import team18.pharmacyapp.model.Pricings;
import team18.pharmacyapp.model.keys.PharmacyMedicinesId;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PricingsRepository extends JpaRepository<Pricings, UUID> {

    @Query("SELECT p FROM pricings p " +
            "JOIN FETCH p.pharmacyMedicine pm " +
            "JOIN FETCH pm.medicine m " +
            "JOIN FETCH pm.pharmacy ph " +
            "WHERE ph.id = :pharmacyId AND p.startDate < :currentDate AND p.endDate > :currentDate")
    List<Pricings> getAllCurrentPricingsForPharmacy(@Param("pharmacyId") UUID pharmacyId, @Param("currentDate") LocalDate currentDate);

    @Query("SELECT p FROM pricings p " +
            "JOIN FETCH p.pharmacyMedicine pm " +
            "JOIN FETCH pm.medicine m " +
            "JOIN FETCH pm.pharmacy ph " +
            "WHERE ph.id = :pharmacyId AND m.id = :medicineId")
    List<Pricings> getAllPricingsForMedicine(@Param("pharmacyId") UUID pharmacyId, @Param("medicineId") UUID medicineId);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM pricings WHERE id = :id")
    void deleteById(UUID id);

    @Transactional(readOnly = true)
    @Query(nativeQuery = true, value = "SELECT COUNT(*) FROM pricings " +
            "WHERE ((start_date <= :startDate AND end_date >= :startDate) OR (start_date <= :endDate AND end_date >= :endDate))" +
            "AND pharmacy_medicine_pharmacy_id = :pharmacyId AND pharmacy_medicine_medicine_id = :medicineId")
    int getNumberOfOverlappingPricings(LocalDate startDate, LocalDate endDate, UUID pharmacyId, UUID medicineId);

    @Transactional(readOnly = true)
    @Query(nativeQuery = true, value = "SELECT COUNT(*) FROM pricings " +
            "WHERE ((start_date <= :startDate AND end_date >= :startDate) OR (start_date <= :endDate AND end_date >= :endDate))" +
            "AND pharmacy_medicine_pharmacy_id = (SELECT pharmacy_medicine_pharmacy_id FROM pricings WHERE id=:pricingId) " +
            "AND pharmacy_medicine_medicine_id = (SELECT pharmacy_medicine_medicine_id FROM pricings WHERE id=:pricingId)")
    int getNumberOfOverlappingPricings(LocalDate startDate, LocalDate endDate, UUID pricingId);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "INSERT INTO pricings (id, start_date, end_date, price, pharmacy_medicine_pharmacy_id, pharmacy_medicine_medicine_id) " +
            "VALUES (:id, :startDate, :endDate, :price, :pharmacyId, :medicineId)")
    int insert(UUID id, LocalDate startDate, LocalDate endDate, double price, UUID pharmacyId, UUID medicineId);

}
