package team18.pharmacyapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team18.pharmacyapp.model.dtos.HandleReservationDTO;
import team18.pharmacyapp.model.dtos.ReservedMedicineResponseDTO;
import team18.pharmacyapp.model.medicine.ReservedMedicines;
import team18.pharmacyapp.repository.ReservedMedicinesRepository;
import team18.pharmacyapp.service.interfaces.EmailService;
import team18.pharmacyapp.service.interfaces.ReservedMedicinesService;

import javax.persistence.LockModeType;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class ReservedMedicinesServiceImpl implements ReservedMedicinesService {
    private final ReservedMedicinesRepository repository;
    private final EmailService emailService;

    @Autowired
    public ReservedMedicinesServiceImpl(ReservedMedicinesRepository repository, EmailService emailService) {
        this.repository = repository;
        this.emailService = emailService;
    }


    @Override
    public ReservedMedicineResponseDTO findByIdAndPharmacy(UUID id, UUID pharmacy) {
        return repository.findByReservationId(id, pharmacy);
    }

    public boolean checkPickupDate(Date pickupDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.HOUR_OF_DAY, 24);
        return cal.getTime().compareTo(pickupDate) <= 0;
    }


    @Override
    public ReservedMedicineResponseDTO checkReservation(UUID id, UUID pharmacy) {
        ReservedMedicineResponseDTO medicines = findByIdAndPharmacy(id, pharmacy);
        if (medicines != null) {
            if (!checkPickupDate(medicines.getPickupDate()) || medicines.isHandled()) {
                return null;
            }
        }
        return medicines;
    }

    @Override
    public List<ReservedMedicineResponseDTO> getAll() {
        return null;
    }

    @Override
    @Transactional
    @Lock(LockModeType.WRITE)
    public boolean handleMedicine(HandleReservationDTO dto) {
        ReservedMedicines medicines = repository.findById(dto.getId()).orElse(null);
        if (medicines != null) {
            medicines.setHandled(true);
            repository.save(medicines);
            String subject = "[ISA Pharmacy] Confirmation - Medicine reservation";
            String body = "You successfully take reserved medicine " + dto.getMedicine() + ".\n" +
                    "Your reservation ID: " + dto.getId().toString();
            new Thread(() -> emailService.sendMail(dto.getEmail(), subject, body)).start();
            return true;
        }
        return false;
    }

}
