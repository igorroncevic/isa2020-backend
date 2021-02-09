package team18.pharmacyapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import team18.pharmacyapp.model.Term;
import team18.pharmacyapp.model.dtos.DoctorScheduleTermDTO;
import team18.pharmacyapp.service.interfaces.TermService;

import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping(value = "api/terms")
public class TermController {
    private final TermService termService;

    @Autowired
    public TermController(TermService termService) {
        this.termService = termService;
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity<Term> saveNewPatient(@RequestBody DoctorScheduleTermDTO dto){
        Term term=termService.scheduleTerm(dto);
        if(term!=null){
            return new ResponseEntity<>(term,HttpStatus.CREATED);
        }
        return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
    }

    @GetMapping("doctor/{id}/{pharmacyId}")
    public List<Term> getAllDoctorTerms(@PathVariable UUID id,@PathVariable UUID pharmacyId) {
        return termService.getAllDoctorTermsInPharmacy(id, pharmacyId);
    }


    //@PreAuthorize("hasRole('ROLE_PATIENT')")
    @GetMapping("/upcoming/{id}")
    public ResponseEntity<List<Term>> getPatientsUpcomingTerms(@PathVariable UUID id){
        List<Term> terms = termService.findAllPatientsUpcomingTerms(id);

        return new ResponseEntity<>(terms,HttpStatus.OK);
    }
}
