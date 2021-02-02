package team18.pharmacyapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team18.pharmacyapp.model.Vacation;
import team18.pharmacyapp.model.enums.VacationStatus;
import team18.pharmacyapp.repository.VacationRepository;
import team18.pharmacyapp.service.interfaces.VacationService;

import java.util.List;
import java.util.UUID;

@Service
public class VacationServiceImpl implements VacationService {

    private final VacationRepository vacationRepository;

    @Autowired
    public VacationServiceImpl(VacationRepository vacationRepository) {
        this.vacationRepository = vacationRepository;
    }

    @Override
    public List<Vacation> getAll(VacationStatus vacationStatus) {
        return vacationRepository.getAll(vacationStatus);
    }

    @Override
    public Vacation getById(UUID vacationId) {
        return vacationRepository.findById(vacationId).orElse(null);
    }

    @Override
    public void approve(UUID vacationId) {
        vacationRepository.approve(vacationId);
    }

    @Override
    public void refuse(UUID vacationId, String reason) {
        vacationRepository.refuse(vacationId, reason);
    }
}