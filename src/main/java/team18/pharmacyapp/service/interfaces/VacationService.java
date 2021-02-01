package team18.pharmacyapp.service.interfaces;

import team18.pharmacyapp.model.Vacation;

import java.util.List;
import java.util.UUID;

public interface VacationService {

    List<Vacation> getAllPending();

    Vacation getById(UUID vacationId);

    void approve(UUID vacationId);

    void refuse(UUID vacationId, String reason);

}
