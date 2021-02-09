package team18.pharmacyapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import team18.pharmacyapp.model.Term;
import team18.pharmacyapp.model.dtos.*;
import team18.pharmacyapp.model.exceptions.ActionNotAllowedException;
import team18.pharmacyapp.model.exceptions.AlreadyScheduledException;
import team18.pharmacyapp.model.exceptions.EntityNotFoundException;
import team18.pharmacyapp.model.exceptions.ScheduleTermException;
import team18.pharmacyapp.service.interfaces.CheckupService;
import team18.pharmacyapp.service.interfaces.TermService;

import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = {"http://localhost:8080","http://localhost:8081"})
@RestController
@RequestMapping(value = "api/checkups")
public class CheckupController {
    private final CheckupService checkupService;
    private final TermService termService;

    @Autowired
    public CheckupController(CheckupService checkupService, TermService termService) {
        this.checkupService = checkupService;
        this.termService = termService;
    }

    @PreAuthorize("hasRole('ROLE_PATIENT')")
    @GetMapping
    public ResponseEntity<List<TermDTO>> getAllAvailableCheckups() {
        List<TermDTO> checkups;

        try {
            checkups = checkupService.findAllAvailableCheckups();
        }catch(RuntimeException e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(checkups, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_PATIENT')")
    @GetMapping("/patient/{id}")
    public ResponseEntity<List<TermDTO>> getAllPatientsCheckups(@PathVariable UUID id) {
        List<TermDTO> checkups;

        try{
            checkups = checkupService.findAllPatientsCheckups(id);
        }catch(RuntimeException e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(checkups, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_PATIENT')")
    @PostMapping("/past")
    public ResponseEntity<TermPaginationDTO> getAllPatientsPastCheckupsPaginated(@RequestBody TermPaginationSortingDTO psDTO) {
        TermPaginationDTO checkups;
        try{
            checkups = termService.findAllPatientsPastTermsPaginated(psDTO.getId(), psDTO.getSort(), psDTO.getTermType(), psDTO.getPage());
        }catch(RuntimeException e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(checkups, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_PATIENT')")
    @PostMapping("/upcoming")
    public ResponseEntity<TermPaginationDTO> getAllPatientsUpcomingCheckupsPaginated(@RequestBody TermPaginationSortingDTO psDTO) {
        TermPaginationDTO checkups;
        try{
            checkups = termService.findPatientsUpcomingTermsByTypePaginated(psDTO.getId(), psDTO.getSort(), psDTO.getTermType(), psDTO.getPage());
        }catch(RuntimeException e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(checkups, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<TermDTO> getCheckup(@PathVariable UUID id) {
        TermDTO checkup = checkupService.findOne(id);

        if (checkup == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(checkup, HttpStatus.OK);
    }

    @GetMapping(value = "/patientCheckup/{id}")
    public ResponseEntity<Term> getCheckupFetchPatient(@PathVariable UUID id) {
        Term checkup = checkupService.findByIdFetchDoctor(id);

        if (checkup == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(checkup, HttpStatus.OK);
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity<Term> saveCheckup(@RequestBody Term checkup) {
        Term savedCheckup = checkupService.save(checkup);
        return new ResponseEntity<>(savedCheckup, HttpStatus.CREATED);
    }

    @PutMapping(consumes = "application/json")
    public ResponseEntity<Term> updateCheckup(@RequestBody Term checkup) {
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteCheckup(@PathVariable UUID id) {
        TermDTO term = checkupService.findOne(id);

        if (term != null) {
            checkupService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasRole('ROLE_PATIENT')")
    @PutMapping(consumes = "application/json", value = "/schedule")
    public ResponseEntity<Void> patientScheduleCheckup(@RequestBody ScheduleCheckupDTO term) {
        boolean success;
        try {
            success = checkupService.patientScheduleCheckup(term);
        } catch (ActionNotAllowedException ex) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } catch (ScheduleTermException ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (AlreadyScheduledException ex) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
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
    public ResponseEntity<Void> patientCancelCheckup(@RequestBody CancelTermDTO term) {
        boolean success;
        try {
            success = checkupService.patientCancelCheckup(term);
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
