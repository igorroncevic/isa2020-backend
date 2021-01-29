package team18.pharmacyapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team18.pharmacyapp.model.Pharmacy;
import team18.pharmacyapp.service.interfaces.PharmacyService;

import java.util.UUID;

@CrossOrigin(origins = {"http://localhost:8080","http://localhost:8081"})
@RestController
@RequestMapping(value = "api/pharmacies")
public class PharmacyController {
    private final PharmacyService pharmacyService;

    @Autowired
    public PharmacyController(PharmacyService pharmacyService) {
        this.pharmacyService = pharmacyService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pharmacy> getById(@PathVariable UUID id) {
        Pharmacy pharmacy = pharmacyService.getById(id);
        pharmacy.getPharmacyMedicines();
        return new ResponseEntity<>(pharmacy, HttpStatus.OK);
    }
}
