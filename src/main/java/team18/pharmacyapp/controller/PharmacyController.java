package team18.pharmacyapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team18.pharmacyapp.helpers.FilteringHelpers;
import team18.pharmacyapp.model.Pharmacy;
import team18.pharmacyapp.model.dtos.PharmacyDTO;
import team18.pharmacyapp.model.dtos.PharmacyFilteringDTO;
import team18.pharmacyapp.service.interfaces.PharmacyService;

import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = {"http://localhost:8080", "http://localhost:8081"})
@RestController
@RequestMapping(value = "api/pharmacies")
public class PharmacyController {
    private final PharmacyService pharmacyService;

    @Autowired
    public PharmacyController(PharmacyService pharmacyService) {
        this.pharmacyService = pharmacyService;
    }

    @GetMapping("/search")
    public ResponseEntity<List<PharmacyFilteringDTO>> getAllForFiltered(@RequestParam(name = "name", required = false) String name, @RequestParam(name = "mark", required = false, defaultValue = "0.0") float mark, @RequestParam(name = "city", required = false) String city) {
        if(!FilteringHelpers.isAlpha(name) || !FilteringHelpers.isAlpha(city))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        List<PharmacyFilteringDTO> pharmacies = pharmacyService.getAllFiltered(name, mark, city);
        return new ResponseEntity<>(pharmacies, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pharmacy> getById(@PathVariable UUID id) {
        Pharmacy pharmacy = pharmacyService.getById(id);
        pharmacy.getPharmacyMedicines();
        return new ResponseEntity<>(pharmacy, HttpStatus.OK);
    }

    @GetMapping("/allpharms")
    public ResponseEntity<List<Pharmacy>> allPharmacies(){
        List<Pharmacy> all = pharmacyService.getAll();
        return new ResponseEntity<>(all, HttpStatus.OK);
    }

    @PostMapping(consumes = "application/json", value = "/register")
    public ResponseEntity<Pharmacy> saveNewPharmacy(@RequestBody PharmacyDTO newPharmacy){
        Pharmacy pharmacy = pharmacyService.registerNewPharmacy(newPharmacy);
        return new ResponseEntity<>(pharmacy, HttpStatus.CREATED);
    }
}
