package team18.pharmacyapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import team18.pharmacyapp.model.Term;
import team18.pharmacyapp.model.dtos.DoctorScheduleTermDTO;
import team18.pharmacyapp.model.dtos.DoctorTermDTO;
import team18.pharmacyapp.model.dtos.TermDTO;
import team18.pharmacyapp.service.interfaces.TermService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "api/terms")
public class TermController {
    private final TermService termService;

    @Autowired
    public TermController(TermService termService) {
        this.termService = termService;
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity<Term> saveTerm(@RequestBody DoctorScheduleTermDTO dto){
        Term term=termService.scheduleTerm(dto);
        if(term!=null){
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PreAuthorize("hasRole('ROLE_PHADMIN') || hasRole('ROLE_DERMATOLOGIST') || hasRole('ROLE_PHARMACIST')")
    @GetMapping("doctor/{id}/{pharmacyId}")
    public List<DoctorTermDTO> getAllDoctorTermsInPharmacy(@PathVariable UUID id, @PathVariable UUID pharmacyId) {
        return termService.getAllDoctorTermsInPharmacy(id, pharmacyId);
    }

    @PreAuthorize("hasRole('ROLE_PHADMIN') || hasRole('ROLE_DERMATOLOGIST') || hasRole('ROLE_PHARMACIST')")
    @GetMapping("doctorAll/{id}")
    public List<DoctorTermDTO> getAllDoctorTerms(@PathVariable UUID id) {
        return termService.getAllDoctorTerms(id);
    }


    @PreAuthorize("hasRole('ROLE_PATIENT')")
    @GetMapping("/upcoming/{id}")
    public ResponseEntity<List<TermDTO>> getPatientsUpcomingTerms(@PathVariable UUID id){
        List<TermDTO> terms;
        try{
            terms = termService.findAllPatientsUpcomingTerms(id);
        }catch(RuntimeException ex){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(terms,HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_PHADMIN') || hasRole('ROLE_DERMATOLOGIST') || hasRole('ROLE_PHARMACIST')")
    @GetMapping("nowTerm/{patientId}/{doctorId}")
    public ResponseEntity<DoctorTermDTO> getNowTerm(@PathVariable UUID patientId,@PathVariable UUID doctorId){
        DoctorTermDTO term=termService.hasPatientHasTermNowWithDoctor(doctorId,patientId);
        if(term!=null){
            return new ResponseEntity<>(term,HttpStatus.OK);
        }
        return new  ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
    }


}
