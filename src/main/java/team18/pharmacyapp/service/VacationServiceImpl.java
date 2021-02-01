package team18.pharmacyapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team18.pharmacyapp.model.Vacation;
import team18.pharmacyapp.repository.VacationRepository;
import team18.pharmacyapp.service.interfaces.VacationService;

import java.util.List;

@Service
public class VacationServiceImpl implements VacationService {

    private final VacationRepository vacationRepository;

    @Autowired
    public VacationServiceImpl(VacationRepository vacationRepository) {
        this.vacationRepository = vacationRepository;
    }

    @Override
    public List<Vacation> getAllPending() {
        return vacationRepository.getAllPending();
    }
}