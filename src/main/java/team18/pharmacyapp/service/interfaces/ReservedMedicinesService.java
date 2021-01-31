package team18.pharmacyapp.service.interfaces;

import team18.pharmacyapp.model.dtos.HandleReservationDTO;
import team18.pharmacyapp.model.dtos.ReservedMedicineResponseDTO;
import team18.pharmacyapp.model.medicine.ReservedMedicines;

import java.util.List;
import java.util.UUID;

public interface ReservedMedicinesService {
    public ReservedMedicineResponseDTO findByIdAndPharmacy(UUID id,UUID pharmacy);
    public ReservedMedicineResponseDTO checkReservation(UUID id,UUID pharmacy);
    public List<ReservedMedicineResponseDTO> getAll();
    public boolean handleMedicine(HandleReservationDTO dto);
}
