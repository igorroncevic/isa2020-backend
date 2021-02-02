package team18.pharmacyapp.controller;

import org.springframework.web.bind.annotation.*;
import team18.pharmacyapp.model.dtos.ReportMedicineDTO;
import team18.pharmacyapp.model.medicine.Medicine;
import team18.pharmacyapp.service.interfaces.PharmacyMedicinesService;

import java.util.List;
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

    @PostMapping("availability")
    public boolean checkavAilability(@RequestBody ReportMedicineDTO dto){
        return medicinesService.checkAvailability(dto);
    }
    @GetMapping("{pharmacyId}")
    public List<Medicine> getPharamacyMedicine(@PathVariable UUID pharmacyId){
        return medicinesService.getMedicnesByPharmacy(pharmacyId);
    }
}
