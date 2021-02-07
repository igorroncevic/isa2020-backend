package team18.pharmacyapp.service.interfaces;

import team18.pharmacyapp.model.dtos.EPrescriptionDTO;
import team18.pharmacyapp.model.exceptions.ActionNotAllowedException;

import java.util.List;
import java.util.UUID;

public interface EPrescriptionService {
    List<EPrescriptionDTO> findAllByPatientId(UUID id) throws RuntimeException, ActionNotAllowedException;
}
