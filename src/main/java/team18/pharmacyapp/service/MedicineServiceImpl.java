package team18.pharmacyapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team18.pharmacyapp.model.Pricings;
import team18.pharmacyapp.model.dtos.*;
import team18.pharmacyapp.model.exceptions.ActionNotAllowedException;
import team18.pharmacyapp.model.exceptions.ReserveMedicineException;
import team18.pharmacyapp.model.medicine.Medicine;
import team18.pharmacyapp.model.medicine.PharmacyMedicines;
import team18.pharmacyapp.model.users.Patient;
import team18.pharmacyapp.repository.MarkRepository;
import team18.pharmacyapp.repository.MedicineRepository;
import team18.pharmacyapp.repository.PharmacyRepository;
import team18.pharmacyapp.service.interfaces.EmailService;
import team18.pharmacyapp.repository.PatientRepository;
import team18.pharmacyapp.service.interfaces.MedicineService;

import java.util.*;

@Service
public class MedicineServiceImpl implements MedicineService {
    private final MedicineRepository medicineRepository;
    private final EmailService emailService;
    private final PatientRepository patientRepository;
    private final MarkRepository markRepository;
    private final PharmacyRepository pharmacyRepository;

    @Autowired
    public MedicineServiceImpl(MedicineRepository medicineRepository, EmailService emailService, PatientRepository patientRepository, MarkRepository markRepository, PharmacyRepository pharmacyRepository) {
        this.medicineRepository = medicineRepository;
        this.emailService = emailService;
        this.patientRepository = patientRepository;
        this.markRepository = markRepository;
        this.pharmacyRepository = pharmacyRepository;
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

    @Transactional(rollbackFor = {ActionNotAllowedException.class, RuntimeException.class, ReserveMedicineException.class})
    @Override
    public boolean reserveMedicine(ReserveMedicineRequestDTO rmrDTO) throws ActionNotAllowedException, ReserveMedicineException, RuntimeException {
        Patient patient = patientRepository.findById(rmrDTO.getPatientId()).orElse(null);
        if(patient == null) throw new ActionNotAllowedException("You are not allowed to reserve medicines");
        if (patient.getPenalties() >= 3)
            throw new ActionNotAllowedException("You are not allowed to reserve medicines");

        if(rmrDTO.getPickupDate().before(new Date())) throw new ActionNotAllowedException("You are not allowed to reserve medicines");

        UUID reservationId = UUID.randomUUID();
        int reserved = medicineRepository.reserveMedicine(reservationId, rmrDTO.getPatientId(), rmrDTO.getPharmacyId(), rmrDTO.getMedicineId(), rmrDTO.getPickupDate());
        int updateQuantity = medicineRepository.decrementMedicineQuantity(rmrDTO.getMedicineId(), rmrDTO.getPharmacyId());

        if (reserved != 1) throw new ReserveMedicineException("Medicine wasn't reserved!");
        if (updateQuantity != 1) throw new ReserveMedicineException("Medicine quantity wasn't decremented!");

        String userMail = "savooroz33@gmail.com";   // zakucano za sada
        String subject = "[ISA Pharmacy] Confirmation - Medicine reservation";
        String body = "You have successfuly reserved a medicine on our site.\n" +
                "Your reservation ID: " + reservationId.toString();
        new Thread(() -> emailService.sendMail(userMail, subject, body)).start();

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

    @Override
    public List<MedicineMarkDTO> getAllMedicinesForMarkingOptimized(UUID patientId) {
        List<Medicine> allMedicines = medicineRepository.getPatientsMedicines(patientId);

        List<MedicineMarkDTO>mFinal = new ArrayList<>();
        for(Medicine m : allMedicines){
            MedicineMarkDTO mmDTO = new MedicineMarkDTO();
            mmDTO.setId(m.getId());
            mmDTO.setName(m.getName());
            Float averageMark = markRepository.getAverageMarkForMedicine(m.getId());
            mmDTO.setMark(averageMark);
            mmDTO.setLoyaltyPoints(m.getLoyaltyPoints());
            mFinal.add(mmDTO);
        }

        return mFinal;
    }

    @Override
    public List<Medicine> getAllMedicinesPatientsNotAlergicTo(UUID id) {
        return medicineRepository.getAllMedicinesPatientsNotAlergicTo(id);
    }

    @Override
    @Transactional(rollbackFor = {ActionNotAllowedException.class, RuntimeException.class})
    public boolean addPatientsAllergy(MedicineAllergyDTO allergy) throws RuntimeException {
        int added = medicineRepository.addNewAllergy(allergy.getPatientId(), allergy.getMedicineId());
        if(added != 1) throw new RuntimeException("Couldnt add allergy");

        return true;
    }

    @Override
    public List<MedicineFilterDTO> filterMedicines(MedicineFilterRequestDTO mfr) {
        List<Medicine>medicines = medicineRepository.getAllMedicinesPatientsNotAlergicTo(mfr.getPatientId());
        List<MedicineFilterDTO>finalMedicines = new ArrayList<>();

        for(Medicine m : medicines){
            if(!m.getName().toLowerCase().contains(mfr.getName().toLowerCase())) continue;

            List<PharmacyFilteringDTO>pharmacies = pharmacyRepository.getAllPharmaciesForMedicine(m.getId());
            if(pharmacies.size() == 0) continue;

            MedicineFilterDTO med = new MedicineFilterDTO();
            med.setMedicine(m);
            med.setPharmacies(pharmacies);
            finalMedicines.add(med);
        }

        return finalMedicines;
    }
}
