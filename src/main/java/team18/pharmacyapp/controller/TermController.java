package team18.pharmacyapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team18.pharmacyapp.model.Term;
import team18.pharmacyapp.model.dtos.TermDTO;
import team18.pharmacyapp.service.TermService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value="api/terms")
public class TermController {
    @Autowired
    private TermService termService;

    @GetMapping
    public ResponseEntity<List<Term>>getAllTerms(){
        List<Term> terms = termService.findAll();

        return new ResponseEntity<>(terms, HttpStatus.OK);
    }

    @GetMapping(value="/{id}")
    public ResponseEntity<Term>getTerm(@PathVariable UUID id){
        Term term = termService.findOne(id);

        if(term == null){
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

    @PutMapping(consumes = "application/json", value="schedule")
    public ResponseEntity<Void> patientScheduleCheckup(Term term){
        boolean success = termService.patientScheduleCheckup(new TermDTO(term));

        if(success){
            return new ResponseEntity<>(HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}
