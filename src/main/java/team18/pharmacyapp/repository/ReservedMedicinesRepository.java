package team18.pharmacyapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import team18.pharmacyapp.model.dtos.ReserveMedicineRequestDTO;
import team18.pharmacyapp.model.dtos.ReservedMedicineDTO;
import team18.pharmacyapp.model.dtos.ReservedMedicineResponseDTO;
import team18.pharmacyapp.model.medicine.ReservedMedicines;
import team18.pharmacyapp.model.users.Patient;

public interface ReservedMedicinesRepository extends JpaRepository<ReservedMedicines, UUID> {

    @Transactional(readOnly = true)
    @Query(value = "SELECT new team18.pharmacyapp.model.dtos.ReservedMedicineResponseDTO( r.id  ,r.patient.id , r.medicine.id" +
            " ,r.pharmacy.id ,r.pickupDate,p.email,m.name,p.name,p.surname,r.handled)" +
            " FROM reserved_medicines r  INNER join Patient p on r.patient.id=p.id inner join medicine m on r.medicine.id=m.id where r.id=:id and r.pharmacy.id=:pharmacy")
    ReservedMedicineResponseDTO findByReservationId(UUID id,UUID pharmacy);
}
