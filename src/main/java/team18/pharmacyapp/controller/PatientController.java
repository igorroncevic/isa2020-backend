package team18.pharmacyapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import team18.pharmacyapp.model.dtos.MedicineIdNameDTO;
import team18.pharmacyapp.model.dtos.PatientDTO;
import team18.pharmacyapp.model.dtos.UpdateProfileDataDTO;
import team18.pharmacyapp.model.exceptions.ActionNotAllowedException;
import team18.pharmacyapp.model.exceptions.EntityNotFoundException;
import team18.pharmacyapp.model.users.Patient;
import team18.pharmacyapp.service.interfaces.PatientService;

import java.util.List;
import java.util.UUID;


@CrossOrigin(origins = {"http://localhost:8080", "http://localhost:8081"})
@RestController
@RequestMapping(value = "api/patients")
public class PatientController {
    private final PatientService patientService;

    @Autowired
    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping
    public List<Patient> getAll() {
        return patientService.findAll();
    }


    @PreAuthorize("hasRole('ROLE_PATIENT')")
    @GetMapping("/profile/{id}")
    public ResponseEntity<PatientDTO> getPatientProfileInfo(@PathVariable UUID id) {
        PatientDTO pat;
        try {
            pat = patientService.getPatientProfileInfo(id);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(pat, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_PATIENT')")
    @PutMapping("/profile")
    public ResponseEntity<Void> updatePatientProfileInfo(@RequestBody UpdateProfileDataDTO patient) {
        boolean success;
        try {
            success = patientService.updatePatientProfileInfo(patient);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (ActionNotAllowedException e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } catch (RuntimeException ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (success) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/activate/{id}")
    public ResponseEntity<Boolean> activateAccount(@PathVariable String id) {
        UUID uuid = UUID.fromString(id);
        return new ResponseEntity<>(patientService.activateAcc(uuid), HttpStatus.OK);
    }

    @PutMapping("/addPenalty/{id}")
    public ResponseEntity<Integer> addPenalty(@PathVariable UUID id) {
        int res = patientService.addPenalty(id);
        if (res == 1)
            return new ResponseEntity<>(res, HttpStatus.OK);
        return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
    }

    @PreAuthorize("hasRole('ROLE_DERMATOLOGIST') || hasRole('ROLE_PHARMACIST') || hasRole('ROLE_PATIENT')")
    @GetMapping("alergicMedicines/{id}")
    public ResponseEntity<List<MedicineIdNameDTO>> getAlergicMedicines(@PathVariable UUID id) {
        return new ResponseEntity<>(patientService.getAlergicTo(id), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_PATIENT')")
    @GetMapping("penalties/{id}")
    public ResponseEntity<Integer> getPatientPenalties(@PathVariable UUID id) {
        return new ResponseEntity<>(patientService.getPatientPenalties(id), HttpStatus.OK);
    }
}
