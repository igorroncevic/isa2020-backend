package team18.pharmacyapp.service.interfaces;

import team18.pharmacyapp.model.dtos.*;
import team18.pharmacyapp.model.exceptions.ActionNotAllowedException;
import team18.pharmacyapp.model.medicine.Medicine;
import team18.pharmacyapp.model.exceptions.ReserveMedicineException;

import java.util.List;
import java.util.UUID;

public interface MedicineService {
    List<Medicine> findAll();

    Medicine save(Medicine medicine);

    List<PharmacyMedicinesDTO> findAllAvailableMedicines();

    Medicine findById(UUID id);

    void deleteById(UUID id);

    List<ReservedMedicineDTO> findAllPatientsReservedMedicines(UUID id);

    boolean reserveMedicine(ReserveMedicineRequestDTO reserveMedicineRequestDTO) throws ReserveMedicineException, RuntimeException, ActionNotAllowedException;

    boolean cancelMedicine(CancelMedicineRequestDTO cmrDTO) throws ReserveMedicineException, RuntimeException;

    List<MedicineMarkDTO> getAllMedicinesForMarking(UUID patientId);

    List<MedicineMarkDTO> getAllMedicinesForMarkingOptimized(UUID patientId);
}
