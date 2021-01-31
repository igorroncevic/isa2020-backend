package team18.pharmacyapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team18.pharmacyapp.model.Term;
import team18.pharmacyapp.model.dtos.*;
import team18.pharmacyapp.model.exceptions.*;
import team18.pharmacyapp.service.interfaces.CounselingService;

import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping(value = "api/counseling")
public class CounselingController {
    private final CounselingService counselingService;

    @Autowired
    public CounselingController(CounselingService counselingService) {
        this.counselingService = counselingService;
    }

    @GetMapping("/patient/{id}")
    public ResponseEntity<List<Term>> getAllPatientsCounselings(@PathVariable UUID id) {
        List<Term> counselings = counselingService.findAllPatientsCounselings(id);

        return new ResponseEntity<>(counselings, HttpStatus.OK);
    }

    @PostMapping("/available")
    public ResponseEntity<List<PharmacyMarkPriceDTO>> getPharmaciesWithAvailableCounselings(@RequestBody DateTimeRangeDTO timeRange) {
        List<PharmacyMarkPriceDTO> pharmacies;
        try{
            pharmacies = counselingService.getPharmaciesWithAvailableCounselings(timeRange);
        }catch(BadTimeRangeException ex){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(pharmacies, HttpStatus.OK);
    }

    @PostMapping("/available/{id}")
    public ResponseEntity<List<DoctorDTO>> getFreeDoctorsForPharmacy(@PathVariable UUID id, @RequestBody DateTimeRangeDTO timeRange) {
        List<DoctorDTO> doctors = counselingService.getFreeDoctorsForPharmacy(id, timeRange);
        return new ResponseEntity<>(doctors, HttpStatus.OK);
    }

    @PostMapping(consumes = "application/json", value = "/schedule")
    public ResponseEntity<Void> patientScheduleCounseling(@RequestBody ScheduleCounselingDTO term) {
        boolean success;
        try {
            success = counselingService.patientScheduleCounseling(term);
        } catch (AlreadyScheduledException ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (ScheduleTermException ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (RuntimeException ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (success) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(consumes = "application/json", value = "/cancel")
    public ResponseEntity<Void> patientCancelCounseling(@RequestBody CancelCounselingDTO term) {
        boolean success;
        try {
            success = counselingService.patientCancelCounseling(term);
        }catch (EntityNotFoundException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (ActionNotAllowedException ex) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (RuntimeException ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (success) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
