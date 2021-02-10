package team18.pharmacyapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team18.pharmacyapp.helpers.DateTimeHelpers;
import team18.pharmacyapp.model.Pharmacy;
import team18.pharmacyapp.model.Pricings;
import team18.pharmacyapp.model.dtos.*;
import team18.pharmacyapp.model.enums.*;
import team18.pharmacyapp.model.exceptions.ActionNotAllowedException;
import team18.pharmacyapp.model.exceptions.ReserveMedicineException;
import team18.pharmacyapp.model.medicine.Medicine;
import team18.pharmacyapp.model.medicine.MedicineSpecification;
import team18.pharmacyapp.model.medicine.PharmacyMedicines;
import team18.pharmacyapp.model.medicine.ReservedMedicines;
import team18.pharmacyapp.model.users.Patient;
import team18.pharmacyapp.repository.MarkRepository;
import team18.pharmacyapp.repository.MedicineRepository;
import team18.pharmacyapp.repository.PharmacyRepository;
import team18.pharmacyapp.repository.MedicineSpecificationRepository;
import team18.pharmacyapp.service.interfaces.EmailService;
import team18.pharmacyapp.repository.users.PatientRepository;
import team18.pharmacyapp.service.interfaces.LoyaltyService;
import team18.pharmacyapp.service.interfaces.MedicineService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class MedicineServiceImpl implements MedicineService {
    private final MedicineRepository medicineRepository;
    private final EmailService emailService;
    private final PatientRepository patientRepository;
    private final MedicineSpecificationRepository medicineSpecificationRepository;
    private final MarkRepository markRepository;
    private final PharmacyRepository pharmacyRepository;
    private final LoyaltyService loyaltyService;


    @Autowired
    public MedicineServiceImpl(MedicineRepository medicineRepository, EmailService emailService, PatientRepository patientRepository, PharmacyRepository pharmacyRepository, MedicineSpecificationRepository medicineSpecificationRepository, MarkRepository markRepository, LoyaltyService loyaltyService) {
        this.medicineRepository = medicineRepository;
        this.emailService = emailService;
        this.patientRepository = patientRepository;
        this.medicineSpecificationRepository = medicineSpecificationRepository;
        this.markRepository = markRepository;
        this.pharmacyRepository = pharmacyRepository;
        this.loyaltyService = loyaltyService;
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
            Medicine med = pm.getMedicine();
            pmDTO.setMedicine(new MedicineDTO(med.getId(), med.getName(), "", "", "", "", "", med.getLoyaltyPoints(), "", 0, "", "", ""));
            Pharmacy pharmacy = pm.getPharmacy();
            pmDTO.setPharmacy(new PharmacyDTO(pharmacy.getId(), pharmacy.getName(), pharmacy.getAddress().getStreet(), pharmacy.getAddress().getCity(), pharmacy.getAddress().getCountry()));
            pmDTO.setPrice(finalPrice);
            pmDTO.setQuantity(pm.getQuantity());

            resultSet.add(pmDTO);
        }

        return resultSet;
    }

    @Override
    public List<ReservedMedicinesDTO> findAllPatientsReservedMedicines(UUID id) {
        Patient pat = patientRepository.getOne(id);
        if(pat == null) throw new RuntimeException("Invalid patient id");

        List<ReservedMedicines> reservedMedicines = medicineRepository.findAllPatientsReservedMedicinesNotPickedUp(id);

        List<ReservedMedicinesDTO> finalReservedMedicines = new ArrayList<>();
        for(ReservedMedicines r : reservedMedicines){
            Medicine med = r.getMedicine();
            MedicineDTO medicineDTO = new MedicineDTO(med.getId(), med.getName(), "", "", "", "", "", med.getLoyaltyPoints(), "", 0, "", "", "");
            Pharmacy pharmacy = r.getPharmacy();
            PharmacyDTO pharmacyDTO = new PharmacyDTO(pharmacy.getId(), pharmacy.getName(), pharmacy.getAddress().getStreet(), pharmacy.getAddress().getCity(), pharmacy.getAddress().getCountry());

            finalReservedMedicines.add(new ReservedMedicinesDTO(r.getId(), medicineDTO, pharmacyDTO, r.getPickupDate(), r.isHandled()));
        }

        return finalReservedMedicines;
    }

    @Transactional(rollbackFor = {ActionNotAllowedException.class, RuntimeException.class, ReserveMedicineException.class})
    @Override
    public boolean reserveMedicine(ReserveMedicineRequestDTO rmrDTO) throws ActionNotAllowedException, ReserveMedicineException, RuntimeException {
        Medicine med = medicineRepository.getOne(rmrDTO.getMedicineId());
        if(med == null) throw new RuntimeException("");

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

        loyaltyService.addLoyaltyPoints(rmrDTO.getPatientId(), med.getLoyaltyPoints());
        loyaltyService.updatePatientsLoyalty(rmrDTO.getPatientId());

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
        Medicine med = medicineRepository.getOne(cmrDTO.getMedicineId());
        if(med == null) throw new RuntimeException("");

        Date reservationDate = medicineRepository.findPickupDateByReservationId(cmrDTO.getReservationId());
        Date tomorrow = new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000);

        tomorrow = DateTimeHelpers.getDateWithoutTime(tomorrow);
        reservationDate = DateTimeHelpers.getDateWithoutTime(reservationDate);

        if (tomorrow.after(reservationDate)) return false;

        int cancelled = medicineRepository.cancelMedicine(cmrDTO.getReservationId());
        int updateQuantity = medicineRepository.incrementMedicineQuantity(cmrDTO.getMedicineId(), cmrDTO.getPharmacyId());

        if (cancelled != 1) throw new ReserveMedicineException("Medicine wasn't cancelled!");
        if (updateQuantity != 1) throw new ReserveMedicineException("Medicine quantity wasn't incremented!");

        loyaltyService.subtractLoyaltyPoints(cmrDTO.getPatientId(), med.getLoyaltyPoints());
        loyaltyService.updatePatientsLoyalty(cmrDTO.getPatientId());

        return true;
    }

    @Override
    public Medicine registerNewMedicine(MedicineDTO medicine) {
        Medicine med = new Medicine();
        MedicineSpecification medSpec = new MedicineSpecification();

        med.setName(medicine.getName());
        med.setMedicineCode(medicine.getMedicineCode());
        med.setMedicineType(MedicineType.valueOf(medicine.getMedicineType()));
        med.setMedicineForm(MedicineForm.valueOf(medicine.getMedicineForm()));
        med.setManufacturer(MedicineManufacturer.valueOf(medicine.getMedicineManufacturer()));
        med.setIssuingRegime(MedicineIssuingRegime.valueOf(medicine.getIssuingRegime()));
        med.setLoyaltyPoints(medicine.getLoyaltyPoints());

        medSpec.setReplacementMedicineCode(medicine.getReplacementMedicine());
        medSpec.setRecommendedDose(medicine.getRecommendedDose());
        medSpec.setContraindications(medicine.getContraindications());
        medSpec.setDrugComposition(medicine.getDrugComposition());
        medSpec.setAdditionalNotes(medicine.getAdditionalNotes());


        med = medicineRepository.save(med);
        medSpec.setMedicine(med);

        medSpec = medicineSpecificationRepository.save(medSpec);
        return med;

    }

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
    public List<MedicineDTO> getAllMedicinesPatientsNotAlergicTo(UUID id) {
        List<Medicine> medicines = medicineRepository.getAllMedicinesPatientsNotAlergicTo(id);
        List<MedicineDTO> finalMedicines = new ArrayList<>();

        for(Medicine med : medicines){
            MedicineDTO medicineDTO = new MedicineDTO(med.getId(), med.getName(), "", "", "", "", "", med.getLoyaltyPoints(), "", 0, "", "", "");
            finalMedicines.add(medicineDTO);
        }

        return finalMedicines;
    }

    @Override
    public List<MedicineDTO> getAllMedicinesPatientsAllergicTo(UUID id){
        List<Medicine> medicines = medicineRepository.getMedicinesPatientsAllergicTo(id);
        List<MedicineDTO> finalMedicines = new ArrayList<>();
        for(Medicine med : medicines){
            MedicineDTO medicineDTO = new MedicineDTO(med.getId(), med.getName(), "", "", "", "", "", med.getLoyaltyPoints(), "", 0, "", "", "");
            finalMedicines.add(medicineDTO);
        }

        return finalMedicines;
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
            med.setMedicine(new MedicineDTO(m.getId(), m.getName(), "", "", "", "", "", m.getLoyaltyPoints(), "", 0, "", "", ""));
            med.setPharmacies(pharmacies);
            finalMedicines.add(med);
        }

        return finalMedicines;
    }
    @Override
    public MedicineSpecification getMedicineSpecification(UUID medicineId) {
        return medicineRepository.getMedicineSpecification(medicineId);
    }

    @Override
    public String getReplacmentMedicine(UUID medicineId) {
        return medicineRepository.getReplacmentMedicine(medicineId);
    }
    @Override
    public List<MedicineFilterDTO> filterNoAuthMedicines(MedicineFilterRequestDTO mfr) {
        List<Medicine>medicines = medicineRepository.findAllAvailableMedicinesNoAuth();

        List<MedicineFilterDTO> finalMedicines = new ArrayList<>();
        for(Medicine m : medicines){
            if(!m.getName().toLowerCase().contains(mfr.getName().toLowerCase())) continue;

            MedicineFilterDTO med = new MedicineFilterDTO();
            med.setMedicine(new MedicineDTO(m.getId(), m.getName(), "", "", "", "", "", m.getLoyaltyPoints(), "", 0, "", "", ""));
            med.setPharmacies(new ArrayList<>());
            finalMedicines.add(med);
        }

        return finalMedicines;
    }
}
