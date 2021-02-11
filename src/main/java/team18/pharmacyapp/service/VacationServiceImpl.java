package team18.pharmacyapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team18.pharmacyapp.model.Vacation;
import team18.pharmacyapp.model.dtos.DoctorDTO;
import team18.pharmacyapp.model.dtos.VacationDTO;
import team18.pharmacyapp.model.dtos.VacationRequestDTO;
import team18.pharmacyapp.model.enums.VacationStatus;
import team18.pharmacyapp.model.exceptions.ActionNotAllowedException;
import team18.pharmacyapp.model.exceptions.EntityNotFoundException;
import team18.pharmacyapp.model.users.Doctor;
import team18.pharmacyapp.repository.VacationRepository;
import team18.pharmacyapp.service.interfaces.DoctorService;
import team18.pharmacyapp.service.interfaces.EmailService;
import team18.pharmacyapp.service.interfaces.VacationService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class VacationServiceImpl implements VacationService {

    private final VacationRepository vacationRepository;
    private final EmailService emailService;
    private final DoctorService doctorService;

    @Autowired
    public VacationServiceImpl(VacationRepository vacationRepository, EmailService emailService, DoctorService doctorService) {
        this.vacationRepository = vacationRepository;
        this.emailService = emailService;
        this.doctorService = doctorService;
    }

    @Override
    public List<VacationDTO> getAll(VacationStatus vacationStatus) {
        List<Vacation> vacations = vacationRepository.getAll(vacationStatus);
        List<VacationDTO> vacationDTOs = new ArrayList<>();
        for(Vacation vacation : vacations) {
            DoctorDTO doctorDTO = new DoctorDTO(vacation.getDoctor().getId(), vacation.getDoctor().getName(),
                    vacation.getDoctor().getSurname(), vacation.getDoctor().getEmail(), vacation.getDoctor().getPhoneNumber(),
                    vacation.getDoctor().getRole(), vacation.getDoctor().getAddress());
            VacationDTO vacationDTO = new VacationDTO(vacation.getId(), vacation.getStartDate(), vacation.getEndDate(),
                    doctorDTO, vacation.getStatus(), vacation.getRejectionReason());
            vacationDTOs.add(vacationDTO);
        }
        return vacationDTOs;
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
    public void refuse(UUID vacationId, String reason) throws EntityNotFoundException, ActionNotAllowedException, RuntimeException, IllegalArgumentException {
        Vacation vacation = vacationRepository.getById(vacationId).orElse(null);
        if(vacation == null)
            throw new EntityNotFoundException("There is no such vacation request");
        else if(vacation.getStatus() != VacationStatus.pending)
            throw new ActionNotAllowedException("You can only refuse pending vacation requests");
        else if(reason.length() < 10 || reason.length() > 255)
            throw new IllegalArgumentException("Reason must be between 10 and 255 characters");

        int rowsUpdated = vacationRepository.refuse(vacationId, reason);
        if (rowsUpdated != 1)
            throw new RuntimeException("Couldn't refuse this vacation request");

        String subject = "[ISA Pharmacy] Vacation request refused";
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String body = "Your vacation request for period of " + sdf.format(vacation.getStartDate()) +
                " - " + sdf.format(vacation.getEndDate()) + "has been refused.\nReason:\n" + reason;
        new Thread(() -> emailService.sendMail(vacation.getDoctor().getEmail(), subject, body)).start();
    }

    @Override
    public Vacation create(VacationRequestDTO vacation) {
        Vacation v=new Vacation();
        v.setEndDate(vacation.getEndDate());
        v.setStartDate(vacation.getStartDate());
        Doctor d=doctorService.getById(vacation.getDoctorId());
        System.out.println(d.getName());
        v.setDoctor(d);
        if(d==null){
            return null;
        }
        v.setStatus(VacationStatus.pending);

        return  vacationRepository.save(v);
    }

}