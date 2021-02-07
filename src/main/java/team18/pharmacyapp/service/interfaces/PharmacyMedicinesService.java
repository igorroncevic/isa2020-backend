package team18.pharmacyapp.service.interfaces;

import team18.pharmacyapp.model.dtos.MedicineQuantityDTO;
import team18.pharmacyapp.model.dtos.ReportMedicineDTO;
import team18.pharmacyapp.model.exceptions.ActionNotAllowedException;
import team18.pharmacyapp.model.keys.PharmacyMedicinesId;
import team18.pharmacyapp.model.medicine.Medicine;
import team18.pharmacyapp.model.medicine.PharmacyMedicines;

import java.util.List;
import java.util.UUID;

public interface PharmacyMedicinesService {
    int medicineQuantity(UUID pharmacyId,UUID medicineId);
    boolean checkAvailability(ReportMedicineDTO dto);
    List<MedicineQuantityDTO> getMedicnesByPharmacy(UUID pharmacy);
    void insert(UUID pharmacyId, UUID medicineId);

    void deletePharmacyMedicine(PharmacyMedicinesId pharmacyMedicinesId) throws ActionNotAllowedException;
}
