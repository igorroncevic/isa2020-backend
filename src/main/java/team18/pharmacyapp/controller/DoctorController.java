package team18.pharmacyapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import team18.pharmacyapp.model.dtos.*;
import team18.pharmacyapp.model.enums.UserRole;
import team18.pharmacyapp.model.users.Doctor;
import team18.pharmacyapp.service.interfaces.DoctorService;

import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = {"http://localhost:8080", "http://localhost:8081"})
@RestController
@RequestMapping(value = "api/doctors")
//@PreAuthorize("hasRole('ROLE_PATIENT')") // dodati npr  || hasRole('ROLE_DOCTOR') ako treba još neki role
public class DoctorController {
    private final DoctorService doctorService;

    @Autowired
    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<DoctorDTO> getById(@PathVariable UUID id) {
        DoctorDTO doctor = doctorService.getDTOyId(id);
        return new ResponseEntity<>(doctor, HttpStatus.OK);
    }

    @PutMapping(consumes = "application/json")
    public ResponseEntity<Doctor> update(@RequestBody Doctor doctor) {
        Doctor doc = doctorService.update(doctor);
        if (doc != null) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PreAuthorize("hasRole('ROLE_PATIENT')")
    @PostMapping("/patient")
    public ResponseEntity<List<DoctorMarkPharmaciesDTO>> getPatientsDoctorsByRole(@RequestBody PatientDoctorRoleDTO patientDoctorRoleDTO) {
        List<DoctorMarkPharmaciesDTO> doctors = doctorService.getPatientsDoctors(patientDoctorRoleDTO);
        if (doctors.size() != 0) {
            return new ResponseEntity<>(doctors, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(doctors, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/dermatologists")
    public ResponseEntity<List<DoctorMarkPharmaciesDTO>> getAllDermatologists() {
        List<DoctorMarkPharmaciesDTO> doctors = doctorService.findAllDoctors(UserRole.dermatologist);
        return new ResponseEntity<>(doctors, HttpStatus.OK);
    }

    @GetMapping("/pharmacy/{pharmacyId}/dermatologists")
    public ResponseEntity<List<DoctorMarkPharmaciesDTO>> getAllDermatologistsForPharmacy(@PathVariable UUID pharmacyId) {
        List<DoctorMarkPharmaciesDTO> doctors = doctorService.findAllDoctorsForPharmacy(pharmacyId, UserRole.dermatologist);
        return new ResponseEntity<>(doctors, HttpStatus.OK);
    }

    @GetMapping("/pharmacists")
    public ResponseEntity<List<DoctorMarkPharmaciesDTO>> getAllPharmacists() {
        List<DoctorMarkPharmaciesDTO> doctors = doctorService.findAllDoctors(UserRole.pharmacist);
        return new ResponseEntity<>(doctors, HttpStatus.OK);
    }

    @GetMapping("/pharmacy/{pharmacyId}/pharmacists")
    public ResponseEntity<List<DoctorMarkPharmaciesDTO>> getAllPharmacistsForPharmacy(@PathVariable UUID pharmacyId) {
        List<DoctorMarkPharmaciesDTO> doctors = doctorService.findAllDoctorsForPharmacy(pharmacyId, UserRole.pharmacist);
        return new ResponseEntity<>(doctors, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_DERMATOLOGIST') || hasRole('ROLE_PHARMACIST')")
    @GetMapping("/patients/{id}")
    public ResponseEntity<List<DoctorsPatientDTO>> getAllPharmacists(@PathVariable UUID id) {
        return new ResponseEntity<>(doctorService.findDoctorsPatients(id), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_DERMATOLOGIST') || hasRole('ROLE_PHARMACIST')")
    @GetMapping("/pharmacyList/{id}")
    public List<PharmacyDTO> getDoctorPharmacyList(@PathVariable UUID id) {
        return doctorService.getDoctorPharmacyList(id);
    }

    @GetMapping("currentPharmacy/{id}")
    public PharmacyDTO getCurrentPharmacu(@PathVariable UUID id) {
        return doctorService.getCurrentPharmacy(id);
    }

    @GetMapping("pharmPharmacy/{id}")
    public PharmacyDTO getPharmacistPharmacy(@PathVariable UUID id) {
        return doctorService.getPharmacistPharmacy(id);
    }
}
