package team18.pharmacyapp.scheduling;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import team18.pharmacyapp.model.Term;
import team18.pharmacyapp.model.medicine.ReservedMedicines;
import team18.pharmacyapp.model.users.Patient;
import team18.pharmacyapp.repository.CheckupRepository;
import team18.pharmacyapp.repository.MedicineRepository;
import team18.pharmacyapp.repository.users.PatientRepository;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Component
@Slf4j
public class SchedulingTasks {
    private final MedicineRepository medicineRepository;
    private final PatientRepository patientRepository;
    private final CheckupRepository checkupRepository;

    @Autowired
    public SchedulingTasks(MedicineRepository medicineRepository, PatientRepository patientRepository, CheckupRepository checkupRepository) {
        this.medicineRepository = medicineRepository;
        this.patientRepository = patientRepository;
        this.checkupRepository = checkupRepository;
    }

    // U ponoc svakog dana
    @Transactional
    @Scheduled(cron = "@daily")
    // @Scheduled(cron="*/10 * * * * ?") // Test with this one
    public void addPenaltyForNotPickingUpReservedMedicines() {
        log.info("Starting Cron Task - Checking if Patient's need to get penalties for not picking up reserved medicines.");
        List<ReservedMedicines> reservations = medicineRepository.findAllReservedMedicines();

        int addedPenalties = 0;

        Date todaysDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        for (ReservedMedicines reservation : reservations) {
            if (sdf.format(todaysDate).equals(sdf.format(reservation.getPickupDate()))) {
                int addedPenalty = patientRepository.addPenalty(reservation.getPatient().getId());
                addedPenalties++;
            }
        }

        log.info("Finishing Cron Task - " + addedPenalties + " total patients received penalties.");
    }

    @Transactional
    @Scheduled(cron = "@hourly")
    public void addPenaltyForNotComingToTerm() {
        log.info("Starting Cron Task - Checking if Patient's need to get penalties for not showing up to a term.");
        List<Term> terms = checkupRepository.findAllWithPatients();

        int addedPenalties = 0;

        Date currentTime = new Date(System.currentTimeMillis() + 5 * 1000);

        for (Term term : terms) {
            if (term.getEndTime().before(currentTime)) {
                int addedPenalty = patientRepository.addPenalty(term.getPatient().getId());
                addedPenalties++;
            }
        }

        log.info("Finishing Cron Task - " + addedPenalties + " total patients received penalties.");
    }

    // U ponoc svakog prvog u mjesecu
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
