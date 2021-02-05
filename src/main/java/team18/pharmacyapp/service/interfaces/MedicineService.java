package team18.pharmacyapp.service.interfaces;

import team18.pharmacyapp.model.dtos.*;
import team18.pharmacyapp.model.exceptions.ActionNotAllowedException;
import team18.pharmacyapp.model.medicine.Medicine;
import team18.pharmacyapp.model.exceptions.ReserveMedicineException;
import team18.pharmacyapp.model.medicine.ReservedMedicines;

import java.util.List;
import java.util.UUID;

public interface MedicineService {
    List<Medicine> findAll();

    Medicine save(Medicine medicine);

    List<PharmacyMedicinesDTO> findAllAvailableMedicines();

    Medicine findById(UUID id);

    void deleteById(UUID id);

    List<ReservedMedicines> findAllPatientsReservedMedicines(UUID id);

    boolean reserveMedicine(ReserveMedicineRequestDTO reserveMedicineRequestDTO) throws ReserveMedicineException, RuntimeException, ActionNotAllowedException;

    boolean cancelMedicine(CancelMedicineRequestDTO cmrDTO) throws ReserveMedicineException, RuntimeException;

    Medicine registerNewMedicine(MedicineDTO medicine);

    List<MedicineMarkDTO> getAllMedicinesForMarkingOptimized(UUID patientId);

    List<Medicine> getAllMedicinesPatientsNotAlergicTo(UUID id);

    boolean addPatientsAllergy(MedicineAllergyDTO allergy) throws RuntimeException;

    List<MedicineFilterDTO> filterMedicines(MedicineFilterRequestDTO mfr);
}
