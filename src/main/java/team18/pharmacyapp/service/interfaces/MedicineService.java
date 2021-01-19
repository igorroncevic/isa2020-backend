package team18.pharmacyapp.service.interfaces;

import team18.pharmacyapp.model.dtos.CancelMedicineRequestDTO;
import team18.pharmacyapp.model.dtos.PharmacyMedicinesDTO;
import team18.pharmacyapp.model.dtos.ReserveMedicineRequestDTO;
import team18.pharmacyapp.model.dtos.ReservedMedicineDTO;
import team18.pharmacyapp.model.medicine.Medicine;
import team18.pharmacyapp.model.medicine.ReserveMedicineException;

import java.util.List;
import java.util.UUID;

public interface MedicineService {
    List<Medicine> findAll();

    Medicine save(Medicine medicine);

    List<PharmacyMedicinesDTO> findAllAvailableMedicines();

    Medicine findById(UUID id);

    void deleteById(UUID id);

    List<ReservedMedicineDTO> findAllPatientsReservedMedicines(UUID id);

    boolean reserveMedicine(ReserveMedicineRequestDTO reserveMedicineRequestDTO) throws ReserveMedicineException, RuntimeException;

    boolean cancelMedicine(CancelMedicineRequestDTO cmrDTO) throws ReserveMedicineException, RuntimeException;
}
