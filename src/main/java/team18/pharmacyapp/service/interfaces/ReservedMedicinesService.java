package team18.pharmacyapp.service.interfaces;

import team18.pharmacyapp.model.dtos.HandleReservationDTO;
import team18.pharmacyapp.model.dtos.ReservedMedicineResponseDTO;

import java.util.List;
import java.util.UUID;

public interface ReservedMedicinesService {
    ReservedMedicineResponseDTO findByIdAndPharmacy(UUID id, UUID pharmacy);

    ReservedMedicineResponseDTO checkReservation(UUID id, UUID pharmacy);

    List<ReservedMedicineResponseDTO> getAll();

    boolean handleMedicine(HandleReservationDTO dto);
}
