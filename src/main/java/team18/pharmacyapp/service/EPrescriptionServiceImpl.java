package team18.pharmacyapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import team18.pharmacyapp.model.Pharmacy;
import team18.pharmacyapp.model.dtos.EPrescriptionDTO;
import team18.pharmacyapp.model.dtos.EPrescriptionMedicinesDTO;
import team18.pharmacyapp.model.dtos.EPrescriptionMedicinesQueryDTO;
import team18.pharmacyapp.model.dtos.EPrescriptionSortFilterDTO;
import team18.pharmacyapp.model.enums.EPrescriptionStatus;
import team18.pharmacyapp.model.exceptions.ActionNotAllowedException;
import team18.pharmacyapp.model.medicine.EPrescription;
import team18.pharmacyapp.model.medicine.Medicine;
import team18.pharmacyapp.model.users.Patient;
import team18.pharmacyapp.repository.EPrescriptionRepository;
import team18.pharmacyapp.repository.MedicineRepository;
import team18.pharmacyapp.repository.PatientRepository;
import team18.pharmacyapp.repository.PharmacyRepository;
import team18.pharmacyapp.service.interfaces.EPrescriptionService;

import java.awt.print.Pageable;
import java.util.*;

@Service
public class EPrescriptionServiceImpl implements EPrescriptionService {
    private final EPrescriptionRepository ePrescriptionRepository;
    private final PatientRepository patientRepository;
    private final MedicineRepository medicineRepository;
    private final PharmacyRepository pharmacyRepository;

    @Autowired
    public EPrescriptionServiceImpl(EPrescriptionRepository ePrescriptionRepository, PatientRepository patientRepository, MedicineRepository medicineRepository, PharmacyRepository pharmacyRepository){
        this.ePrescriptionRepository = ePrescriptionRepository;
        this.patientRepository = patientRepository;
        this.medicineRepository = medicineRepository;
        this.pharmacyRepository = pharmacyRepository;
    }

    @Override
    public List<EPrescriptionDTO> findAllByPatientId(EPrescriptionSortFilterDTO efs) throws RuntimeException, ActionNotAllowedException {
        Patient patient = patientRepository.findById(efs.getPatientId()).orElse(null);
        if(patient == null) throw new ActionNotAllowedException("Patient not found");

        /* Imao sam jako puno problema koje su mi izazivale veze izmedju EPrescription-a i EPrescriptionMedicines-a
           i ovo je bio jedini nacin.

           1. Desavale se su cirkularne veze jer EPrescriptionMedicines sadrzi EPrescription koji vec imam u query-u
           2. Kada sam napravio DTO za EPrescriptionMedicines, nisam mogao da JOIN FETCH-ujem PharmacyMedicines iz
              EPrescriptionMedicines, tj vracao bi mi NULL za Pharmacy i Medicine

           Na kraju sam morao posebno izvlaciti ID-eve Pharmacy-a i Medicine-a iz EPrescriptionMedicines i pribaviti
           ih preko njihovog zasebnog findById. Daleko od optimalnog, ali nisam uspio odraditi bolje.
        */
        EPrescriptionStatus filter;
        if(efs.getFilter().equalsIgnoreCase("All of them")){
            filter = null;
        }else{
            filter = EPrescriptionStatus.valueOf(efs.getFilter().toLowerCase());
        }

        List<EPrescription> prescriptions = ePrescriptionRepository.findAllByPatientIdAndStatus(efs.getPatientId(), filter);
        List<EPrescriptionDTO> finalEPrescriptions = new ArrayList<>();
        for(EPrescription e : prescriptions){
            List<EPrescriptionMedicinesQueryDTO> medicinesQuery = ePrescriptionRepository.findEPrescriptionMedicines(e.getId());

            List<EPrescriptionMedicinesDTO> medicines = new ArrayList<>();
            for(EPrescriptionMedicinesQueryDTO epq : medicinesQuery){
                Medicine med = medicineRepository.findById(epq.getMedicineId()).orElse(null);
                Pharmacy pharmacy = pharmacyRepository.findById(epq.getPharmacyId()).orElse(null);
                medicines.add(new EPrescriptionMedicinesDTO(pharmacy, med, epq.getQuantity()));
            }

            EPrescriptionDTO ePrescriptionDTO = new EPrescriptionDTO();
            ePrescriptionDTO.setId(e.getId());
            ePrescriptionDTO.setEPrescriptionMedicines(medicines);
            ePrescriptionDTO.setIssueDate(e.getIssueDate());
            ePrescriptionDTO.setStatus(e.getStatus());
            finalEPrescriptions.add(ePrescriptionDTO);
        }

        String[] sortParts = efs.getSort().split(" ");
        if(sortParts.length == 3){
            if(sortParts[2].equalsIgnoreCase("asc."))
                finalEPrescriptions.sort(Comparator.comparing(EPrescriptionDTO::getIssueDate));
            else
                finalEPrescriptions.sort(Comparator.comparing(EPrescriptionDTO::getIssueDate).reversed());
        }

        return finalEPrescriptions;
    }
}
