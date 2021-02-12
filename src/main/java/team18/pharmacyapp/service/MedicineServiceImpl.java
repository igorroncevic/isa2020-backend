package team18.pharmacyapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team18.pharmacyapp.helpers.DateTimeHelpers;
import team18.pharmacyapp.model.Pharmacy;
import team18.pharmacyapp.model.Pricings;
import team18.pharmacyapp.model.dtos.*;
import team18.pharmacyapp.model.enums.MedicineForm;
import team18.pharmacyapp.model.enums.MedicineIssuingRegime;
import team18.pharmacyapp.model.enums.MedicineManufacturer;
import team18.pharmacyapp.model.enums.MedicineType;
import team18.pharmacyapp.model.exceptions.ActionNotAllowedException;
import team18.pharmacyapp.model.exceptions.ReserveMedicineException;
import team18.pharmacyapp.model.medicine.*;
import team18.pharmacyapp.model.users.Patient;
import team18.pharmacyapp.repository.*;
import team18.pharmacyapp.service.interfaces.EmailService;
import team18.pharmacyapp.repository.MarkRepository;
import team18.pharmacyapp.repository.MedicineRepository;
import team18.pharmacyapp.repository.MedicineSpecificationRepository;
import team18.pharmacyapp.repository.PharmacyRepository;
import team18.pharmacyapp.repository.users.PatientRepository;
import team18.pharmacyapp.service.interfaces.LoyaltyService;
import team18.pharmacyapp.service.interfaces.MedicineService;

import javax.persistence.LockModeType;
import java.time.LocalDate;
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
    private final SupplierMedicinesRepository supplierMedicinesRepository;
    private final ReservedMedicinesRepository reservedMedicinesRepository;
    private final PharmacyMedicinesRepository pharmacyMedicinesRepository;

    @Autowired
    public MedicineServiceImpl(MedicineRepository medicineRepository, EmailService emailService, PatientRepository patientRepository, PharmacyRepository pharmacyRepository, MedicineSpecificationRepository medicineSpecificationRepository, MarkRepository markRepository, LoyaltyService loyaltyService, SupplierMedicinesRepository supplierMedicinesRepository, ReservedMedicinesRepository reservedMedicinesRepository, PharmacyMedicinesRepository pharmacyMedicinesRepository) {
        this.medicineRepository = medicineRepository;
        this.emailService = emailService;
        this.patientRepository = patientRepository;
        this.medicineSpecificationRepository = medicineSpecificationRepository;
        this.markRepository = markRepository;
        this.pharmacyRepository = pharmacyRepository;
        this.loyaltyService = loyaltyService;
        this.supplierMedicinesRepository = supplierMedicinesRepository;
        this.reservedMedicinesRepository = reservedMedicinesRepository;
        this.pharmacyMedicinesRepository = pharmacyMedicinesRepository;
    }

    @Override
    public List<MedicineIdNameDTO> findAll() {
        List<Medicine> medicines = medicineRepository.findAll();
        List<MedicineIdNameDTO> medicineIdNameDTOs = new ArrayList<>();
        for (Medicine medicine : medicines) {
            MedicineIdNameDTO medicineIdNameDTO = new MedicineIdNameDTO(medicine.getId(), medicine.getName());
            medicineIdNameDTOs.add(medicineIdNameDTO);
        }
        return medicineIdNameDTOs;
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
    @Transactional(readOnly = true)
    public List<PharmacyMedicinesDTO> findAllAvailableMedicines() {
        List<PharmacyMedicines> pharmacyMedicines = medicineRepository.findAllAvailableMedicines();
        List<PharmacyMedicinesDTO> resultSet = new ArrayList<>();

        LocalDate todaysDate = LocalDate.now();   //mali offset
        for (PharmacyMedicines pm : pharmacyMedicines) {
            List<Pricings> pricings = pm.getPricings();
            double finalPrice = -1.0;

            for (Pricings pricing : pricings) {
                if (pricing.getStartDate().isBefore(todaysDate) && pricing.getEndDate().isAfter(todaysDate)) {
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
    @Transactional(readOnly = true)
    public List<ReservedMedicinesDTO> findAllPatientsReservedMedicines(UUID id) {
        Patient pat = patientRepository.getOne(id);
        if (pat == null) throw new RuntimeException("Invalid patient id");

        List<ReservedMedicines> reservedMedicines = medicineRepository.findAllPatientsReservedMedicinesNotPickedUp(id);

        List<ReservedMedicinesDTO> finalReservedMedicines = new ArrayList<>();
        for (ReservedMedicines r : reservedMedicines) {
            Medicine med = r.getMedicine();
            MedicineDTO medicineDTO = new MedicineDTO(med.getId(), med.getName(), "", "", "", "", "", med.getLoyaltyPoints(), "", 0, "", "", "");
            Pharmacy pharmacy = r.getPharmacy();
            PharmacyDTO pharmacyDTO = new PharmacyDTO(pharmacy.getId(), pharmacy.getName(), pharmacy.getAddress().getStreet(), pharmacy.getAddress().getCity(), pharmacy.getAddress().getCountry());

            finalReservedMedicines.add(new ReservedMedicinesDTO(r.getId(), medicineDTO, pharmacyDTO, r.getPickupDate(), r.isHandled()));
        }

        return finalReservedMedicines;
    }

    @Override
    @Lock(LockModeType.WRITE)
    @Transactional(rollbackFor = {ActionNotAllowedException.class, RuntimeException.class})
    public boolean reserveMedicine(ReserveMedicineRequestDTO rmrDTO) throws ActionNotAllowedException, RuntimeException {
        Medicine med = medicineRepository.getOne(rmrDTO.getMedicineId());
        if (med == null) throw new RuntimeException("Medicine does not exist");

        Patient patient = patientRepository.findById(rmrDTO.getPatientId()).orElse(null);
        if (patient == null) throw new ActionNotAllowedException("You are not allowed to reserve medicines");
        if (patient.getPenalties() >= 3)
            throw new ActionNotAllowedException("You are not allowed to reserve medicines");

        if (rmrDTO.getPickupDate().before(new Date()))
            throw new ActionNotAllowedException("You are not allowed to reserve medicines");

        Pharmacy pharmacy = new Pharmacy();
        pharmacy.setId(rmrDTO.getPharmacyId());

        if(!isPharmacyMedicineAvailable(med, pharmacy))
            throw new ActionNotAllowedException("Medicine out of stock");

        //int reserved = medicineRepository.reserveMedicine(reservationId, rmrDTO.getPatientId(), rmrDTO.getPharmacyId(), rmrDTO.getMedicineId(), rmrDTO.getPickupDate());
        //int updateQuantity = medicineRepository.decrementMedicineQuantity(rmrDTO.getMedicineId(), rmrDTO.getPharmacyId());
        //if (reserved != 1) throw new ReserveMedicineException("Medicine wasn't reserved!");
        //if (updateQuantity != 1) throw new ReserveMedicineException("Medicine quantity wasn't decremented!");

        ReservedMedicines reservation = new ReservedMedicines(UUID.randomUUID(), patient, med, pharmacy, rmrDTO.getPickupDate(), false, 0L);
        reservation = reservedMedicinesRepository.save(reservation);

        PharmacyMedicines pharmacyMedicines = pharmacyMedicinesRepository.findDistinctByPharmacyAndMedicine(pharmacy, med);
        pharmacyMedicines.setQuantity(pharmacyMedicines.getQuantity() - 1);
        pharmacyMedicinesRepository.save(pharmacyMedicines);

        loyaltyService.addLoyaltyPoints(rmrDTO.getPatientId(), med.getLoyaltyPoints());
        loyaltyService.updatePatientsLoyalty(rmrDTO.getPatientId());

        String userMail = patient.getEmail();
        String subject = "[ISA Pharmacy] Confirmation - Medicine reservation";
        String body = "You have successfuly reserved a medicine on our site.\n" +
                "Your reservation ID: " + reservation.getId().toString();
        new Thread(() -> emailService.sendMail(userMail, subject, body)).start();

        return true;
    }

    public boolean isPharmacyMedicineAvailable(Medicine med, Pharmacy pharmacy){
        return pharmacyMedicinesRepository.getMedicineQuantity(med, pharmacy) > 0;
    }

    @Override
    @Transactional(rollbackFor = {RuntimeException.class, ReserveMedicineException.class})
    public boolean cancelMedicine(CancelMedicineRequestDTO cmrDTO) throws ReserveMedicineException, RuntimeException {
        Medicine med = medicineRepository.getOne(cmrDTO.getMedicineId());
        if (med == null) throw new RuntimeException("");

        Date reservationDate = medicineRepository.findPickupDateByReservationId(cmrDTO.getReservationId());
        Date tomorrow = new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000);

        tomorrow = DateTimeHelpers.getDateWithoutTime(tomorrow);
        reservationDate = DateTimeHelpers.getDateWithoutTime(reservationDate);

        if (tomorrow.compareTo(reservationDate) >= 0) return false;

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

    @Override
    @Transactional(readOnly = true)
    public List<MedicineMarkDTO> getAllMedicinesForMarkingOptimized(UUID patientId) {
        List<Medicine> allMedicines = medicineRepository.getPatientsMedicines(patientId);

        List<MedicineMarkDTO> mFinal = new ArrayList<>();
        for (Medicine m : allMedicines) {
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
    @Transactional(readOnly = true)
    public List<MedicineDTO> getAllMedicinesPatientsNotAlergicTo(UUID id) {
        List<Medicine> medicines = medicineRepository.getAllMedicinesPatientsNotAlergicTo(id);
        List<MedicineDTO> finalMedicines = new ArrayList<>();

        for (Medicine med : medicines) {
            MedicineDTO medicineDTO = new MedicineDTO(med.getId(), med.getName(), "", "", "", "", "", med.getLoyaltyPoints(), "", 0, "", "", "");
            finalMedicines.add(medicineDTO);
        }

        return finalMedicines;
    }

    @Override
    @Transactional(readOnly = true)
    public List<MedicineDTO> getAllMedicinesPatientsAllergicTo(UUID id) {
        List<Medicine> medicines = medicineRepository.getMedicinesPatientsAllergicTo(id);
        List<MedicineDTO> finalMedicines = new ArrayList<>();
        for (Medicine med : medicines) {
            MedicineDTO medicineDTO = new MedicineDTO(med.getId(), med.getName(), "", "", "", "", "", med.getLoyaltyPoints(), "", 0, "", "", "");
            finalMedicines.add(medicineDTO);
        }

        return finalMedicines;
    }

    @Override
    @Transactional(rollbackFor = {ActionNotAllowedException.class, RuntimeException.class})
    public boolean addPatientsAllergy(MedicineAllergyDTO allergy) throws RuntimeException {
        int added = medicineRepository.addNewAllergy(allergy.getPatientId(), allergy.getMedicineId());
        if (added != 1) throw new RuntimeException("Couldnt add allergy");

        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public List<MedicineFilterDTO> filterMedicines(MedicineFilterRequestDTO mfr) {
        List<Medicine> medicines = medicineRepository.getAllMedicinesPatientsNotAlergicTo(mfr.getPatientId());
        List<MedicineFilterDTO> finalMedicines = new ArrayList<>();

        for (Medicine m : medicines) {
            if (!m.getName().toLowerCase().contains(mfr.getName().toLowerCase())) continue;

            List<PharmacyFilteringDTO> pharmacies = pharmacyRepository.getAllPharmaciesForMedicine(m.getId());
            if (pharmacies.size() == 0) continue;

            MedicineFilterDTO med = new MedicineFilterDTO();
            med.setMedicine(new MedicineDTO(m.getId(), m.getName(), "", "", "", "", "", m.getLoyaltyPoints(), "", 0, "", "", ""));
            med.setPharmacies(pharmacies);
            finalMedicines.add(med);
        }

        return finalMedicines;
    }

    @Override
    public List<SupplierMedicinesDTO> findSupplierMedicines(UUID supplierId) {
        List<SupplierMedicine> meds = medicineRepository.findMedicinesBySupplierId(supplierId);
        List<SupplierMedicinesDTO> ret = new ArrayList<>();
        for (SupplierMedicine sm : meds) {
            SupplierMedicinesDTO dto = new SupplierMedicinesDTO();
            dto.setSupplierId(supplierId);
            dto.setMedicineName(sm.getMedicine().getName());
            dto.setQuantity(sm.getQuantity());
            ret.add(dto);
        }
        return ret;
    }

    @Override
    public SupplierMedicine addNewSupplierMedicine(SupplierMedicinesDTO supplierMedicinesDTO) {
        SupplierMedicine supplierMedicine = new SupplierMedicine();
        Medicine m = medicineRepository.findByName(supplierMedicinesDTO.getMedicineName());
        if (m != null) {
            if (supplierMedicinesRepository.findByName(m.getName()) != null) {
                int ret = supplierMedicinesRepository.updateMedicineQuantity(m.getId(), supplierMedicinesDTO.getQuantity());
                return supplierMedicine;
            }
            int ret = supplierMedicinesRepository.addSupplierMedicine(supplierMedicinesDTO.getSupplierId(), m.getId(), supplierMedicinesDTO.getQuantity());
            return supplierMedicine;
        }
        return null;
    }

    @Override
    public MedicineSpecificationDTO getMedicineSpecification(UUID medicineId) {
        MedicineSpecification ms = medicineRepository.getMedicineSpecification(medicineId);
        return new MedicineSpecificationDTO(ms.getReplacementMedicineCode(), ms.getRecommendedDose(), ms.getContraindications(), ms.getDrugComposition(), ms.getAdditionalNotes());
    }

    @Override
    public String getReplacmentMedicine(UUID medicineId) {
        return medicineRepository.getReplacmentMedicine(medicineId);
    }

    @Override
    public List<MedicineFilterDTO> filterNoAuthMedicines(MedicineFilterRequestDTO mfr) {
        List<Medicine> medicines = medicineRepository.findAllAvailableMedicinesNoAuth();

        List<MedicineFilterDTO> finalMedicines = new ArrayList<>();
        for (Medicine m : medicines) {
            if (!m.getName().toLowerCase().contains(mfr.getName().toLowerCase())) continue;

            MedicineFilterDTO med = new MedicineFilterDTO();
            med.setMedicine(new MedicineDTO(m.getId(), m.getName(), "", "", "", "", "", m.getLoyaltyPoints(), "", 0, "", "", ""));
            med.setPharmacies(new ArrayList<>());
            finalMedicines.add(med);
        }

        return finalMedicines;
    }
}
