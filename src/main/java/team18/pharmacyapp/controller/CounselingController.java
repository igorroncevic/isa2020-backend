package team18.pharmacyapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import team18.pharmacyapp.model.dtos.*;
import team18.pharmacyapp.model.exceptions.*;
import team18.pharmacyapp.service.interfaces.CounselingService;
import team18.pharmacyapp.service.interfaces.TermService;

import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = {"http://localhost:8080", "http://localhost:8081"})
@RestController
@RequestMapping(value = "api/counseling")
public class CounselingController {
    private final CounselingService counselingService;
    private final TermService termService;

    @Autowired
    public CounselingController(CounselingService counselingService, TermService termService) {
        this.counselingService = counselingService;
        this.termService = termService;
    }

    @PreAuthorize("hasRole('ROLE_PATIENT')")
    @GetMapping("/patient/{id}")
    public ResponseEntity<List<TermDTO>> getAllPatientsCounselings(@PathVariable UUID id) {
        List<TermDTO> counselings;

        try {
            counselings = counselingService.findAllPatientsCounselings(id);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(counselings, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_PATIENT')")
    @PostMapping("/past")
    public ResponseEntity<TermPaginationDTO> getAllPatientsPastCounselingsPaginated(@RequestBody TermPaginationSortingDTO psDTO) {
        TermPaginationDTO counselings;
        try {
            counselings = termService.findAllPatientsPastTermsPaginated(psDTO.getId(), psDTO.getSort(), psDTO.getTermType(), psDTO.getPage());
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(counselings, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_PATIENT')")
    @PostMapping("/upcoming")
    public ResponseEntity<TermPaginationDTO> getAllPatientsUpcomingCheckupsPaginated(@RequestBody TermPaginationSortingDTO psDTO) {
        TermPaginationDTO counselings;

        try {
            counselings = termService.findPatientsUpcomingTermsByTypePaginated(psDTO.getId(), psDTO.getSort(), psDTO.getTermType(), psDTO.getPage());
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(counselings, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_PATIENT')")
    @PostMapping("/available")
    public ResponseEntity<List<PharmacyMarkPriceDTO>> getPharmaciesWithAvailableCounselings(@RequestBody DateTimeRangeDTO timeRange) {
        List<PharmacyMarkPriceDTO> pharmacies;
        try {
            pharmacies = counselingService.getPharmaciesWithAvailableCounselings(timeRange);
        } catch (BadTimeRangeException ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(pharmacies, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_PATIENT')")
    @PostMapping("/available/{id}")
    public ResponseEntity<List<DoctorMarkPharmaciesDTO>> getFreeDoctorsForPharmacy(@PathVariable UUID id, @RequestBody DateTimeRangeDTO timeRange) {
        List<DoctorMarkPharmaciesDTO> doctors;

        try {
            doctors = counselingService.getFreeDoctorsForPharmacy(id, timeRange);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(doctors, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_PATIENT')")
    @PostMapping(consumes = "application/json", value = "/schedule")
    public ResponseEntity<Void> patientScheduleCounseling(@RequestBody ScheduleCounselingDTO term) {
        boolean success;
        try {
            success = counselingService.patientScheduleCounseling(term);
        } catch (AlreadyScheduledException ex) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
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

    @PreAuthorize("hasRole('ROLE_PATIENT')")
    @PutMapping(consumes = "application/json", value = "/cancel")
    public ResponseEntity<Void> patientCancelCounseling(@RequestBody CancelTermDTO term) {
        boolean success;
        try {
            success = counselingService.patientCancelCounseling(term);
        } catch (EntityNotFoundException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (ActionNotAllowedException ex) {
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
