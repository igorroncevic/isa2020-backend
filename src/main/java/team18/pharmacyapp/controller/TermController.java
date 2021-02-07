package team18.pharmacyapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team18.pharmacyapp.model.Term;
import team18.pharmacyapp.model.dtos.DoctorScheduleTermDTO;
import team18.pharmacyapp.service.interfaces.TermService;

import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = {"http://localhost:8080","http://localhost:8081"})
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
    @GetMapping("doctor/{id}")
    public List<Term> getAllDoctorTerms(@PathVariable UUID id){
        return termService.getAllDoctorTerms(id);
    }

    @GetMapping("doctor/{id}/{pharmacyId}")
    public List<Term> getAllDoctorTerms(@PathVariable UUID id,@PathVariable UUID pharmacyId){
        return termService.getAllDoctorTermsInPharmacy(id,pharmacyId);
    }

    @GetMapping("nowTerm/{patientId}/{doctorId}")
    public ResponseEntity<Term> getNowTerm(@PathVariable UUID patientId,@PathVariable UUID doctorId){
        Term term=termService.hasPatientHasTermNowWithDoctor(doctorId,patientId);
        if(term!=null){
            return new ResponseEntity<>(term,HttpStatus.OK);
        }
        return new  ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
    }


}
