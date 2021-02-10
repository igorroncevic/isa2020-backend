package team18.pharmacyapp.service.interfaces;

import team18.pharmacyapp.model.medicine.MedicineRequests;

import java.util.UUID;

public interface MedicineRequestsService {
    MedicineRequests checkRequest(UUID medicineId,UUID pharmacyId);
}
