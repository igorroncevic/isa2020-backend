package team18.pharmacyapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team18.pharmacyapp.model.Term;
import team18.pharmacyapp.model.dtos.ScheduleCheckupDTO;
import team18.pharmacyapp.service.interfaces.CheckupService;

import java.util.*;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping(value = "api/checkups")
public class CheckupController {
    private final CheckupService checkupService;

    @Autowired
    public CheckupController(CheckupService checkupService) {
        this.checkupService = checkupService;
    }

    @GetMapping
    public ResponseEntity<List<Term>> getAllAvailableCheckups() {
        List<Term> checkups = checkupService.findAllAvailableCheckups();

        return new ResponseEntity<>(checkups, HttpStatus.OK);
    }

    @GetMapping("/patient/{id}")
    public ResponseEntity<List<Term>> getAllPatientsCheckups(@PathVariable UUID id) {
        List<Term> checkups = checkupService.findAllPatientsCheckups(id);

        return new ResponseEntity<>(checkups, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Term> getCheckup(@PathVariable UUID id) {
        Term checkup = checkupService.findOne(id);

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
        Term checkupForUpdate = checkupService.findOne(checkup.getId());

        if (checkupForUpdate == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        checkupForUpdate.setDoctor(checkup.getDoctor());
        checkupForUpdate.setStartTime(checkup.getStartTime());
        checkupForUpdate.setEndTime(checkup.getEndTime());
        checkupForUpdate.setPatient(checkup.getPatient());
        checkupForUpdate.setPrice(checkup.getPrice());
        checkupForUpdate.setReport(checkup.getReport());
        checkupForUpdate.setLoyaltyPoints(checkup.getLoyaltyPoints());

        checkupForUpdate = checkupService.save(checkupForUpdate);
        return new ResponseEntity<>(checkupForUpdate, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteCheckup(@PathVariable UUID id) {
        Term term = checkupService.findOne(id);

        if (term != null) {
            checkupService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(consumes = "application/json", value = "/schedule")
    public ResponseEntity<Void> patientScheduleCheckup(@RequestBody ScheduleCheckupDTO term) {
        boolean success = checkupService.patientScheduleCheckup(term);

        if (success) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(consumes = "application/json", value = "/cancel")
    public ResponseEntity<Void> patientCancelCheckup(@RequestBody ScheduleCheckupDTO term) {
        boolean success = checkupService.patientCancelCheckup(term);

        if (success) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}
