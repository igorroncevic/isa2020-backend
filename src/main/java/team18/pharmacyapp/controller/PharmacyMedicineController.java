package team18.pharmacyapp.controller;

import org.springframework.web.bind.annotation.*;
import team18.pharmacyapp.model.dtos.ReportMedicineDTO;
import team18.pharmacyapp.service.interfaces.PharmacyMedicinesService;

import java.util.UUID;

@CrossOrigin(origins = {"http://localhost:8080","http://localhost:8081"})
@RestController
@RequestMapping(value = "api/pharmacyMedicines")
public class PharmacyMedicineController {
    private final PharmacyMedicinesService medicinesService;

    public PharmacyMedicineController(PharmacyMedicinesService medicinesService) {
        this.medicinesService = medicinesService;
    }

    @GetMapping("{pharmacyId}/{medicineId}")
    public int getQyantity(@PathVariable UUID pharmacyId,@PathVariable UUID medicineId){
        return medicinesService.medicineQuantity(pharmacyId,medicineId);
    }

    @GetMapping("availability")
    public boolean checkavAilability(@RequestBody ReportMedicineDTO dto){
        return medicinesService.checkAvailability(dto);
    }
}
