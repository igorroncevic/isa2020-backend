package team18.pharmacyapp.service.interfaces;

import team18.pharmacyapp.model.dtos.MedicineIdNameDTO;
import team18.pharmacyapp.model.dtos.ReportMedicineDTO;
import team18.pharmacyapp.model.medicine.Medicine;

import java.util.List;
import java.util.UUID;

public interface PharmacyMedicinesService {
    int medicineQuantity(UUID pharmacyId,UUID medicineId);
    boolean checkAvailability(ReportMedicineDTO dto);
    List<MedicineIdNameDTO> getMedicnesByPharmacy(UUID pharmacy);
}
