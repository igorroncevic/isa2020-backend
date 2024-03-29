package team18.pharmacyapp.service.interfaces;

import team18.pharmacyapp.model.Vacation;
import team18.pharmacyapp.model.dtos.VacationDTO;
import team18.pharmacyapp.model.dtos.VacationRequestDTO;
import team18.pharmacyapp.model.enums.UserRole;
import team18.pharmacyapp.model.enums.VacationStatus;
import team18.pharmacyapp.model.exceptions.ActionNotAllowedException;
import team18.pharmacyapp.model.exceptions.EntityNotFoundException;

import java.util.List;
import java.util.UUID;

public interface VacationService {

    List<VacationDTO> getAll(VacationStatus vacationStatus, UserRole userRole);

    void approve(UUID vacationId) throws EntityNotFoundException, ActionNotAllowedException;

    void refuse(UUID vacationId, String reason) throws EntityNotFoundException, ActionNotAllowedException;

    Vacation create(VacationRequestDTO vacation);

}
