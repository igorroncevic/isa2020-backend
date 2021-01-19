package team18.pharmacyapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team18.pharmacyapp.model.Pricings;
import team18.pharmacyapp.model.dtos.CancelMedicineRequestDTO;
import team18.pharmacyapp.model.dtos.PharmacyMedicinesDTO;
import team18.pharmacyapp.model.dtos.ReserveMedicineRequestDTO;
import team18.pharmacyapp.model.dtos.ReservedMedicineDTO;
import team18.pharmacyapp.model.exceptions.ActionNotAllowedException;
import team18.pharmacyapp.model.medicine.Medicine;
import team18.pharmacyapp.model.medicine.PharmacyMedicines;
import team18.pharmacyapp.model.medicine.ReserveMedicineException;
import team18.pharmacyapp.model.users.Patient;
import team18.pharmacyapp.repository.MedicineRepository;
import team18.pharmacyapp.repository.PatientRepository;
import team18.pharmacyapp.service.interfaces.MedicineService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class MedicineServiceImpl implements MedicineService {
    private MedicineRepository medicineRepository;
    private PatientRepository patientRepository;

    @Autowired
    public MedicineServiceImpl(MedicineRepository medicineRepository, PatientRepository patientRepository) {
        this.medicineRepository = medicineRepository;
        this.patientRepository = patientRepository;
    }

    @Override
    public List<Medicine> findAll() {
        return medicineRepository.findAll();
    }

    @Override
    public Medicine save(Medicine medicine) {
        return medicineRepository.save(medicine);
    }

    @Override
    public Medicine findById(UUID id) {
        return medicineRepository.findById(id).orElseGet(null);
    }

    @Override
    public void deleteById(UUID id) {
        medicineRepository.deleteById(id);
    }

    @Override
    public List<PharmacyMedicinesDTO> findAllAvailableMedicines() {
        List<PharmacyMedicines> pharmacyMedicines = medicineRepository.findAllAvailableMedicines();
        List<PharmacyMedicinesDTO> resultSet = new ArrayList<>();

        Date todaysDate = new Date(System.currentTimeMillis() + 10 * 1000);   //mali offset
        for (PharmacyMedicines pm : pharmacyMedicines) {
            List<Pricings> pricings = pm.getPricings();
            double finalPrice = -1.0;

            for (Pricings pricing : pricings) {
                if (pricing.getStartDate().before(todaysDate) && pricing.getEndDate().after(todaysDate)) {
                    finalPrice = pricing.getPrice();
                    break;
                }
            }

            if (finalPrice == -1) continue;

            PharmacyMedicinesDTO pmDTO = new PharmacyMedicinesDTO();
            pmDTO.setMedicine(pm.getMedicine());
            pmDTO.setPharmacy(pm.getPharmacy());
            pmDTO.setPrice(finalPrice);
            pmDTO.setQuantity(pm.getQuantity());

            resultSet.add(pmDTO);
        }

        return resultSet;
    }

    @Override
    public List<ReservedMedicineDTO> findAllPatientsReservedMedicines(UUID id) {
        Patient patient = new Patient();
        patient.setId(id);
        List<ReservedMedicineDTO> reservedMedicines = medicineRepository.findAllPatientsReservedMedicines(patient);
        List<ReservedMedicineDTO> resultSet = new ArrayList<>();

        for (ReservedMedicineDTO rmDTO : reservedMedicines) {
            List<Pricings> pricings = rmDTO.getPricings();
            double finalPrice = -1.0;

            for (Pricings pricing : pricings) {
                if (pricing.getStartDate().before(rmDTO.getPickupDate()) && pricing.getEndDate().after(rmDTO.getPickupDate())) {
                    finalPrice = pricing.getPrice();
                    break;
                }
            }

            if (finalPrice == -1.0) continue;

            ReservedMedicineDTO finalRmDTO = new ReservedMedicineDTO();
            finalRmDTO.setMedicine(rmDTO.getMedicine());
            finalRmDTO.setPharmacy(rmDTO.getPharmacy());
            finalRmDTO.setPrice(finalPrice);
            finalRmDTO.setPickupDate(rmDTO.getPickupDate());

            resultSet.add(finalRmDTO);
        }

        return resultSet;
    }

    @Transactional(rollbackFor = {RuntimeException.class, ReserveMedicineException.class})
    @Override
    public boolean reserveMedicine(ReserveMedicineRequestDTO rmrDTO) throws ActionNotAllowedException, ReserveMedicineException, RuntimeException {
        Patient patient = patientRepository.getOne(rmrDTO.getPatientId());
        if (patient.getPenalties() >= 3) throw new ActionNotAllowedException("You are not allowed to reserve medicines");

        int reserved = medicineRepository.reserveMedicine(UUID.randomUUID(), rmrDTO.getPatientId(), rmrDTO.getPharmacyId(), rmrDTO.getMedicineId(), rmrDTO.getPickupDate());
        int updateQuantity = medicineRepository.decrementMedicineQuantity(rmrDTO.getMedicineId(), rmrDTO.getPharmacyId());

        if (reserved != 1) throw new ReserveMedicineException("Medicine wasn't reserved!");
        if (updateQuantity != 1) throw new ReserveMedicineException("Medicine quantity wasn't decremented!");

        return true;
    }

    @Transactional(rollbackFor = {RuntimeException.class, ReserveMedicineException.class})
    @Override
    public boolean cancelMedicine(CancelMedicineRequestDTO cmrDTO) throws ReserveMedicineException, RuntimeException {
        Date reservationDate = medicineRepository.findPickupDateByReservationId(cmrDTO.getReservationId());
        Date yesterday = new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000);

        if (yesterday.before(reservationDate)) return false;

        int cancelled = medicineRepository.cancelMedicine(cmrDTO.getReservationId());
        int updateQuantity = medicineRepository.incrementMedicineQuantity(cmrDTO.getMedicineId(), cmrDTO.getPharmacyId());

        if (cancelled != 1) throw new ReserveMedicineException("Medicine wasn't cancelled!");
        if (updateQuantity != 1) throw new ReserveMedicineException("Medicine quantity wasn't incremented!");

        return true;
    }
}
