package team18.pharmacyapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team18.pharmacyapp.model.dtos.PharmacyMedicinesDTO;
import team18.pharmacyapp.model.dtos.ReserveMedicineDTO;
import team18.pharmacyapp.model.medicine.Medicine;
import team18.pharmacyapp.model.medicine.PharmacyMedicines;
import team18.pharmacyapp.service.interfaces.MedicineService;

import java.util.*;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping(value = "api/medicines")
public class MedicineController {
    private final MedicineService medicineService;

    @Autowired
    public MedicineController(MedicineService medicineService) {
        this.medicineService = medicineService;
    }

    @GetMapping
    public ResponseEntity<List<PharmacyMedicinesDTO>> getAllAvailableMedicines() {
        List<PharmacyMedicinesDTO> medicines = medicineService.findAllAvailableMedicines();

        return new ResponseEntity<>(medicines, HttpStatus.OK);
    }

    @GetMapping("/patient/{id}")
    public ResponseEntity<List<Medicine>> getAllPatientsMedicines(@PathVariable UUID id) {
        List<Medicine> medicines = medicineService.findAllPatientsReservedMedicines(id);

        return new ResponseEntity<>(medicines, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Medicine> getMedicine(@PathVariable UUID id) {
        Medicine medicine = medicineService.findById(id);

        if (medicine == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(medicine, HttpStatus.OK);
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity<Medicine> saveMedicine(@RequestBody Medicine medicine) {
        Medicine savedMedicine = medicineService.save(medicine);
        return new ResponseEntity<>(savedMedicine, HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteMedicine(@PathVariable UUID id) {
        Medicine medicine = medicineService.findById(id);

        if (medicine != null) {
            medicineService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(consumes = "application/json", value = "/schedule")
    public ResponseEntity<Void> reserveMedicine(@RequestBody ReserveMedicineDTO medicine) {
        boolean success = medicineService.reserveMedicine(medicine);

        if (success) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(consumes = "application/json", value = "/cancel")
    public ResponseEntity<Void> cancelMedicine(@RequestBody ReserveMedicineDTO medicine) {
        boolean success = medicineService.cancelMedicine(medicine);

        if (success) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}
