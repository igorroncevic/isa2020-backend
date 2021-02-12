package team18.pharmacyapp.scheduling;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import team18.pharmacyapp.model.Pharmacy;
import team18.pharmacyapp.model.Term;
import team18.pharmacyapp.model.medicine.PharmacyMedicines;
import team18.pharmacyapp.model.medicine.ReservedMedicines;
import team18.pharmacyapp.model.users.Patient;
import team18.pharmacyapp.repository.MedicineRepository;
import team18.pharmacyapp.repository.PharmacyMedicinesRepository;
import team18.pharmacyapp.repository.ReservedMedicinesRepository;
import team18.pharmacyapp.repository.TermRepository;
import team18.pharmacyapp.repository.users.PatientRepository;

import javax.persistence.LockModeType;
import java.util.Date;
import java.util.List;

@Component
@Slf4j
public class SchedulingTasks {
    private final MedicineRepository medicineRepository;
    private final PatientRepository patientRepository;
    private final TermRepository termRepository;
    private final ReservedMedicinesRepository reservedMedicinesRepository;
    private final PharmacyMedicinesRepository pharmacyMedicinesRepository;

    @Autowired
    public SchedulingTasks(MedicineRepository medicineRepository, PatientRepository patientRepository, TermRepository termRepository, ReservedMedicinesRepository reservedMedicinesRepository, PharmacyMedicinesRepository pharmacyMedicinesRepository) {
        this.medicineRepository = medicineRepository;
        this.patientRepository = patientRepository;
        this.termRepository = termRepository;
        this.reservedMedicinesRepository = reservedMedicinesRepository;
        this.pharmacyMedicinesRepository = pharmacyMedicinesRepository;
    }

    @Transactional
    @Lock(LockModeType.WRITE)
    @Scheduled(cron = "@daily")
    public void addPenaltyForNotPickingUpReservedMedicines() {
        log.info("Starting Cron Task - Checking if Patient's need to get penalties for not picking up reserved medicines.");
        List<ReservedMedicines> reservations = medicineRepository.findAllNonHandledReservedMedicines(new Date());

        int addedPenalties = 0;

        Date todaysDate = new Date();
        for (ReservedMedicines reservation : reservations) {
            if (todaysDate.after(reservation.getPickupDate())) {
                int addedPenalty = patientRepository.addPenalty(reservation.getPatient().getId());
                addedPenalties++;

                // Rijesena rezervacija
                reservation.setHandled(true);
                reservedMedicinesRepository.save(reservation);

                // Vracanje kolicine lijeka na prethodno stanje
                PharmacyMedicines pharmacyMedicine =
                        pharmacyMedicinesRepository.findDistinctByPharmacyAndMedicine(reservation.getPharmacy(), reservation.getMedicine());
                pharmacyMedicine.setQuantity(pharmacyMedicine.getQuantity() + 1);
                pharmacyMedicinesRepository.save(pharmacyMedicine);
            }
        }

        log.info("Finishing Cron Task - " + addedPenalties + " total patients received penalties.");
    }

    @Transactional
    @Lock(LockModeType.WRITE)
    @Scheduled(cron = "@daily")
    public void addPenaltyForNotComingToTerm() {
        log.info("Starting Cron Task - Checking if Patient's need to get penalties for not showing up to a term.");
        List<Term> terms = termRepository.findAllWithPatients(new Date());

        int addedPenalties = 0;

        Date currentTime = new Date(System.currentTimeMillis() + 5 * 1000);

        for (Term term : terms) {
            if (term.getEndTime().before(currentTime)) {
                int addedPenalty = patientRepository.addPenalty(term.getPatient().getId());
                addedPenalties++;

                term.setCompleted(true);
                termRepository.save(term);
            }
        }

        log.info("Finishing Cron Task - " + addedPenalties + " total patients received penalties.");
    }

    @Transactional
    @Lock(LockModeType.WRITE)
    @Scheduled(cron = "@monthly")
    public void resetPatientPenalties() {
        log.info("Starting Cron Task - Checking if Patient's need to get penalties.");
        List<Patient> patients = patientRepository.findAll();

        for (Patient patient : patients) {
            int clearedPenalty = patientRepository.resetPenalties(patient.getId());
        }

        log.info("Finishing Cron Task - Reseted all patients' penalties.");
    }

}
