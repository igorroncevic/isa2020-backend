package team18.pharmacyapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team18.pharmacyapp.model.Term;
import team18.pharmacyapp.model.dtos.ScheduleTermDTO;
import team18.pharmacyapp.model.enums.TermType;
import team18.pharmacyapp.service.interfaces.TermService;

import java.util.*;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping(value = "api/terms")
public class TermController {
    private final TermService termService;
    private Set possibleTypes;
    private final String defaultTermType;

    @Autowired
    public TermController(TermService termService) {
        this.termService = termService;

        this.defaultTermType = "def";

        this.possibleTypes = new HashSet();
        this.possibleTypes.add(TermType.checkup.toString());
        this.possibleTypes.add(TermType.counseling.toString());
        this.possibleTypes.add(defaultTermType);  // ako nije poslat type, ovo je default vrijednost
    }

    @GetMapping("/schedule")
    public ResponseEntity<List<Term>> getAllAvailableTerms(@RequestParam(name="type", defaultValue = "def", required = false) String type) {
        if (!possibleTypes.contains(type.toLowerCase()))
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);

        List<Term> terms;

        if (type.equalsIgnoreCase(defaultTermType))
            terms = termService.findAllAvailableTerms();
        else
            terms = termService.findAllAvailableTermsByType(TermType.valueOf(type.toLowerCase()));


        return new ResponseEntity<>(terms, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Term> getTerm(@PathVariable UUID id) {
        Term term = termService.findOne(id);

        if (term == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(term, HttpStatus.OK);
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity<Term> saveTerm(@RequestBody Term term) {
        Term savedTerm = termService.save(term);
        return new ResponseEntity<>(savedTerm, HttpStatus.CREATED);
    }

    @PutMapping(consumes = "application/json")
    public ResponseEntity<Term> updateTerm(@RequestBody Term term) {
        Term termForUpdate = termService.findOne(term.getId());

        if (termForUpdate == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        termForUpdate.setDoctor(term.getDoctor());
        termForUpdate.setStartTime(term.getStartTime());
        termForUpdate.setEndTime(term.getEndTime());
        termForUpdate.setPatient(term.getPatient());
        termForUpdate.setPrice(term.getPrice());
        termForUpdate.setReport(term.getReport());
        termForUpdate.setLoyaltyPoints(term.getLoyaltyPoints());

        termForUpdate = termService.save(termForUpdate);
        return new ResponseEntity<>(termForUpdate, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteTerm(@PathVariable UUID id) {
        Term term = termService.findOne(id);

        if (term != null) {
            termService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(consumes = "application/json", value = "checkup/schedule")
    public ResponseEntity<Void> patientScheduleCheckup(@RequestBody ScheduleTermDTO term) {
        boolean success = termService.patientScheduleCheckup(term);

        if (success) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(consumes = "application/json", value = "checkup/cancel")
    public ResponseEntity<Void> patientCancelCheckup(@RequestBody ScheduleTermDTO term) {
        boolean success = termService.patientCancelCheckup(term);

        if (success) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}
