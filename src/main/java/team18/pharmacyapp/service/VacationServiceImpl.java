package team18.pharmacyapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import team18.pharmacyapp.model.Vacation;
import team18.pharmacyapp.model.enums.VacationStatus;
import team18.pharmacyapp.model.exceptions.ActionNotAllowedException;
import team18.pharmacyapp.model.exceptions.EntityNotFoundException;
import team18.pharmacyapp.repository.VacationRepository;
import team18.pharmacyapp.service.interfaces.EmailService;
import team18.pharmacyapp.service.interfaces.VacationService;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.UUID;

@Service
public class VacationServiceImpl implements VacationService {

    private final VacationRepository vacationRepository;
    private final EmailService emailService;

    @Autowired
    public VacationServiceImpl(VacationRepository vacationRepository, EmailService emailService) {
        this.vacationRepository = vacationRepository;
        this.emailService = emailService;
    }

    @Override
    public List<Vacation> getAll(VacationStatus vacationStatus) {
        return vacationRepository.getAll(vacationStatus);
    }

    @Override
    public void approve(UUID vacationId) throws EntityNotFoundException, ActionNotAllowedException, RuntimeException {
        Vacation vacation = vacationRepository.getById(vacationId).orElse(null);
        if(vacation == null)
            throw new EntityNotFoundException("There is no such vacation request");
        else if(vacation.getStatus() != VacationStatus.pending)
            throw new ActionNotAllowedException("You can only approve pending vacation requests");

        int rowsUpdated = vacationRepository.approve(vacationId);
        if (rowsUpdated != 1)
            throw new RuntimeException("Couldn't approve this vacation request");

        String subject = "[ISA Pharmacy] Vacation request approved";
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String body = "Your vacation request for period of " + sdf.format(vacation.getStartDate()) +
                " - " + sdf.format(vacation.getEndDate()) + "has been approved.";
        new Thread(() -> emailService.sendMail(vacation.getDoctor().getEmail(), subject, body)).start();
    }

    @Override
    public void refuse(UUID vacationId, String reason) throws EntityNotFoundException, ActionNotAllowedException, RuntimeException {
        Vacation vacation = vacationRepository.getById(vacationId).orElse(null);
        if(vacation == null)
            throw new EntityNotFoundException("There is no such vacation request");
        else if(vacation.getStatus() != VacationStatus.pending)
            throw new ActionNotAllowedException("You can only refuse pending vacation requests");

        int rowsUpdated = vacationRepository.refuse(vacationId, reason);
        if (rowsUpdated != 1)
            throw new RuntimeException("Couldn't refuse this vacation request");

        String subject = "[ISA Pharmacy] Vacation request refused";
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String body = "Your vacation request for period of " + sdf.format(vacation.getStartDate()) +
                " - " + sdf.format(vacation.getEndDate()) + "has been refused.\nReason:\n" + reason;
        new Thread(() -> emailService.sendMail(vacation.getDoctor().getEmail(), subject, body)).start();
    }
}