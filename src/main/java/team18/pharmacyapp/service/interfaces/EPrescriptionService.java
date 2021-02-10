package team18.pharmacyapp.service.interfaces;

import team18.pharmacyapp.model.dtos.EPrescriptionDTO;
import team18.pharmacyapp.model.dtos.EPrescriptionSortFilterDTO;
import team18.pharmacyapp.model.exceptions.ActionNotAllowedException;

import java.util.List;

public interface EPrescriptionService {
    List<EPrescriptionDTO> findAllByPatientId(EPrescriptionSortFilterDTO esf) throws RuntimeException, ActionNotAllowedException;
}
