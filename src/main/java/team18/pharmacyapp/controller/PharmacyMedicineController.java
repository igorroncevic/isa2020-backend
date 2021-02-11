package team18.pharmacyapp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team18.pharmacyapp.model.Pharmacy;
import team18.pharmacyapp.model.Term;
import team18.pharmacyapp.model.dtos.AddPharmacyMedicineDTO;
import team18.pharmacyapp.model.dtos.MedicineQuantityDTO;
import team18.pharmacyapp.model.dtos.MedicineIdNameDTO;
import team18.pharmacyapp.model.dtos.ReportMedicineDTO;
import team18.pharmacyapp.model.dtos.ScheduleCheckupDTO;
import team18.pharmacyapp.model.exceptions.ActionNotAllowedException;
import team18.pharmacyapp.model.keys.PharmacyMedicinesId;
import team18.pharmacyapp.model.medicine.Medicine;
import team18.pharmacyapp.model.medicine.PharmacyMedicines;
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
    public String checkavAilability(@RequestBody ReportMedicineDTO dto){
        return medicinesService.checkAvailability(dto);
    }

    @GetMapping("{pharmacyId}")
    public List<MedicineQuantityDTO> getPharamacyMedicine(@PathVariable UUID pharmacyId){
        return medicinesService.getMedicnesByPharmacy(pharmacyId);
    }

    @PostMapping("")
    public ResponseEntity addNewMedicineToPharmacy(@RequestBody AddPharmacyMedicineDTO addPharmacyMedicineDTO){
        medicinesService.insert(addPharmacyMedicineDTO.getPharmacyId(), addPharmacyMedicineDTO.getMedicineId());

        return new ResponseEntity(HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/{pid}/medicine/{mid}")
    public ResponseEntity deletePharmacyMedicine(@PathVariable UUID pid, @PathVariable UUID mid) {
        PharmacyMedicinesId pharmacyMedicinesId = new PharmacyMedicinesId();
        pharmacyMedicinesId.setPharmacy(pid);
        pharmacyMedicinesId.setMedicine(mid);
        try {
            medicinesService.deletePharmacyMedicine(pharmacyMedicinesId);
        } catch (ActionNotAllowedException e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(HttpStatus.OK);
    }
}
