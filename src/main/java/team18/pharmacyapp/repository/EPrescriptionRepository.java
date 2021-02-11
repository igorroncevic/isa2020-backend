package team18.pharmacyapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import team18.pharmacyapp.model.dtos.EPrescriptionMedicinesQueryDTO;
import team18.pharmacyapp.model.enums.EPrescriptionStatus;
import team18.pharmacyapp.model.medicine.EPrescription;

import java.util.List;
import java.util.UUID;

public interface EPrescriptionRepository extends JpaRepository<EPrescription, UUID> {

    @Transactional(readOnly = true)
    @Query("SELECT ep FROM EPrescription ep WHERE ep.patient.id = :patientId AND (:status IS NULL OR ep.status = :status)")
    List<EPrescription> findAllByPatientIdAndStatus(@Param("patientId") UUID patientId, @Param("status") EPrescriptionStatus status);

    @Transactional(readOnly = true)
    @Query("SELECT new team18.pharmacyapp.model.dtos.EPrescriptionMedicinesQueryDTO(pm.pharmacy.id, pm.medicine.id, epm.quantity) " +
            "FROM EPrescriptionMedicines epm JOIN epm.pharmacyMedicines pm " +
            "WHERE epm.ePrescription.id = :ePrescriptionId")
    List<EPrescriptionMedicinesQueryDTO> findEPrescriptionMedicines(@Param("ePrescriptionId") UUID ePrescriptionId);
}
