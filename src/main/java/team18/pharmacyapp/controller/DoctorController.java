package team18.pharmacyapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team18.pharmacyapp.model.dtos.DoctorDTO;
import team18.pharmacyapp.model.dtos.DoctorsPatientDTO;
import team18.pharmacyapp.model.dtos.PatientDoctorRoleDTO;
import team18.pharmacyapp.model.enums.UserRole;
import team18.pharmacyapp.model.users.Doctor;
import team18.pharmacyapp.service.interfaces.DoctorService;

import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = {"http://localhost:8080","http://localhost:8081"})
@RestController
@RequestMapping(value = "api/doctors")
public class DoctorController {
    private final DoctorService doctorService;

    @Autowired
    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Doctor> getById(@PathVariable UUID id) {
        Doctor doctor=doctorService.getById(id);
        return new ResponseEntity<>(doctor, HttpStatus.OK);
    }

    @PutMapping(consumes = "application/json")
    public ResponseEntity<Doctor> update(@RequestBody Doctor doctor) {
        Doctor doc=doctorService.update(doctor);
        if (doc!=null) {
            return new ResponseEntity<>(doc, HttpStatus.OK);
        }
        return new ResponseEntity<>(doc, HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/patient")
    public ResponseEntity<List<DoctorDTO>> getPatientsDoctorsByRole(@RequestBody PatientDoctorRoleDTO patientDoctorRoleDTO) {
        List<DoctorDTO> doctors = doctorService.getPatientsDoctors(patientDoctorRoleDTO);
        if(doctors.size() != 0) {
            return new ResponseEntity<>(doctors, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(doctors, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/dermatologists")
    public ResponseEntity<List<DoctorDTO>> getAllDermatologists() {
        List<DoctorDTO> doctors = doctorService.findAllDoctors(UserRole.dermatologist);
        return new ResponseEntity<>(doctors, HttpStatus.OK);
    }

    @GetMapping("/pharmacy/{pharmacyId}/dermatologists")
    public ResponseEntity<List<DoctorDTO>> getAllDermatologistsForPharmacy(@PathVariable UUID pharmacyId) {
        List<DoctorDTO> doctors = doctorService.findAllDoctorsForPharmacy(pharmacyId, UserRole.dermatologist);
        return new ResponseEntity<>(doctors, HttpStatus.OK);
    }

    @GetMapping("/pharmacists")
    public ResponseEntity<List<DoctorDTO>> getAllPharmacists() {
        List<DoctorDTO> doctors = doctorService.findAllDoctors(UserRole.pharmacist);
        return new ResponseEntity<>(doctors, HttpStatus.OK);
    }

    @GetMapping("/pharmacy/{pharmacyId}/pharmacists")
    public ResponseEntity<List<DoctorDTO>> getAllPharmacistsForPharmacy(@PathVariable UUID pharmacyId) {
        List<DoctorDTO> doctors = doctorService.findAllDoctorsForPharmacy(pharmacyId, UserRole.pharmacist);
        return new ResponseEntity<>(doctors, HttpStatus.OK);
    }

    @GetMapping("/patients/{id}")
    public ResponseEntity<List<DoctorsPatientDTO>> getAllPharmacists(@PathVariable UUID id) {
        return new ResponseEntity<>(doctorService.findDoctorsPatients(id), HttpStatus.OK);
    }
}
