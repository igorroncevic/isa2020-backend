package team18.pharmacyapp.service.interfaces;

import team18.pharmacyapp.model.dtos.PharmacyMedicinesDTO;
import team18.pharmacyapp.model.dtos.ReserveMedicineDTO;
import team18.pharmacyapp.model.medicine.Medicine;
import team18.pharmacyapp.model.medicine.PharmacyMedicines;

import java.util.List;
import java.util.UUID;

public interface MedicineService {
    List<Medicine> findAll();
    Medicine save(Medicine medicine);
    List<PharmacyMedicinesDTO> findAllAvailableMedicines();
    Medicine findById(UUID id);
    void deleteById(UUID id);
    List<Medicine> findAllPatientsReservedMedicines(UUID id);
    boolean reserveMedicine(ReserveMedicineDTO reserveMedicineDTO);
    boolean cancelMedicine(ReserveMedicineDTO reserveMedicineDTO);
}
