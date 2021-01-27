package team18.pharmacyapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team18.pharmacyapp.model.dtos.DoctorDTO;
import team18.pharmacyapp.model.enums.UserRole;
import team18.pharmacyapp.service.interfaces.DoctorService;

import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping(value = "api/doctors")
public class DoctorController {
    private final DoctorService doctorService;

    @Autowired
    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
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
}
