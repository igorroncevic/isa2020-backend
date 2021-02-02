package team18.pharmacyapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team18.pharmacyapp.model.dtos.LoginPatientDTO;
import team18.pharmacyapp.model.dtos.RegisterPatientDTO;
import team18.pharmacyapp.model.medicine.Medicine;
import team18.pharmacyapp.model.users.Patient;
import team18.pharmacyapp.service.PatientServiceImpl;

import java.util.List;
import java.util.UUID;


@CrossOrigin(origins = {"http://localhost:8080","http://localhost:8081"})
@RestController
@RequestMapping(value = "api/patients")
public class PatientController {
    private final PatientServiceImpl patientService;

    @Autowired
    public PatientController(PatientServiceImpl patientService) {
        this.patientService = patientService;
    }

    @GetMapping
    public List<Patient> getAll() {
        return patientService.findAll();
    }


    @PostMapping("/login")
    public ResponseEntity<Patient> login(@RequestBody LoginPatientDTO patient){
        Patient pat = patientService.findRegisteredPatient(patient);
        if (pat != null && pat.isActivated()) return new ResponseEntity<>(pat, HttpStatus.OK);

        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @PostMapping(consumes = "application/json", value = "/register")
    public ResponseEntity<Patient> saveNewPatient(@RequestBody RegisterPatientDTO newPatient){
        Patient patient = patientService.register(newPatient);
        return new ResponseEntity<>(patient, HttpStatus.CREATED);
    }

    @PutMapping("/activate/{id}")
    public ResponseEntity<Boolean> activateAccount(@PathVariable String id){
        UUID uuid=UUID.fromString(id);
        return new ResponseEntity<>(patientService.activateAccount(uuid),HttpStatus.OK);
    }
    @PutMapping("/addPenalty/{id}")
    public ResponseEntity<Integer> addPenalty(@PathVariable UUID id){
        int res=patientService.addPenalty(id);
        if(res==1)
            return new ResponseEntity<>(res,HttpStatus.OK);
        return new ResponseEntity<>(res,HttpStatus.BAD_REQUEST);
    }

    @GetMapping("alergicMedicines/{id}")
    public ResponseEntity<List<Medicine>> getAlergicMedicines(@PathVariable UUID id){
        return new ResponseEntity<>(patientService.getAlergicTo(id),HttpStatus.OK);
    }
}
