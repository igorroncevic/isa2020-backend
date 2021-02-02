package team18.pharmacyapp.service.interfaces;

import team18.pharmacyapp.model.dtos.ReportMedicineDTO;

import java.util.UUID;

public interface PharmacyMedicinesService {
    int medicineQuantity(UUID pharmacyId,UUID medicineId);
    boolean checkAvailability(ReportMedicineDTO dto);
}
