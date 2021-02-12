package team18.pharmacyapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team18.pharmacyapp.model.dtos.AddPharmacyMedicineDTO;
import team18.pharmacyapp.model.dtos.MedicineQuantityDTO;
import team18.pharmacyapp.model.dtos.PharmacyDTO;
import team18.pharmacyapp.model.dtos.ReportMedicineDTO;
import team18.pharmacyapp.model.exceptions.ActionNotAllowedException;
import team18.pharmacyapp.model.keys.PharmacyMedicinesId;
import team18.pharmacyapp.security.TokenUtils;
import team18.pharmacyapp.service.interfaces.PharmacyAdminService;
import team18.pharmacyapp.service.interfaces.PharmacyMedicinesService;
import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = {"http://localhost:8080","http://localhost:8081"})
@RestController
@RequestMapping(value = "api/pharmacyMedicines")
public class PharmacyMedicineController {
    private final PharmacyMedicinesService medicinesService;
    private final TokenUtils tokenUtils;
    private final PharmacyAdminService pharmacyAdminService;

    @Autowired
    public PharmacyMedicineController(PharmacyMedicinesService medicinesService, TokenUtils tokenUtils, PharmacyAdminService pharmacyAdminService) {
        this.medicinesService = medicinesService;
        this.tokenUtils = tokenUtils;
        this.pharmacyAdminService = pharmacyAdminService;
    }


    @GetMapping("{pharmacyId}/{medicineId}")
    public int getQyantity(@PathVariable UUID pharmacyId,@PathVariable UUID medicineId){
        return medicinesService.medicineQuantity(pharmacyId,medicineId);
    }

    @PreAuthorize("hasRole('ROLE_DERMATOLOGIST') || hasRole('ROLE_PHARMACIST')")
    @PostMapping("availability")
    public String checkavAilability(@RequestBody ReportMedicineDTO dto){
        return medicinesService.checkAvailability(dto);
    }

    @GetMapping("{pharmacyId}")
    public List<MedicineQuantityDTO> getPharamacyMedicine(@PathVariable UUID pharmacyId){
        return medicinesService.getMedicnesByPharmacy(pharmacyId);
    }

    @PreAuthorize("hasRole('ROLE_PHADMIN')")
    @PostMapping("")
    public ResponseEntity addNewMedicineToPharmacy(@RequestBody AddPharmacyMedicineDTO addPharmacyMedicineDTO, @RequestHeader("Authorization") String token) {
        UUID phadminId = tokenUtils.getUserIdFromToken(token.split(" ")[1]);
        PharmacyDTO pharmacyDTO = pharmacyAdminService.getPharmacyAdminPharmacyId(phadminId);
        addPharmacyMedicineDTO.setPharmacyId(pharmacyDTO.getId());
        medicinesService.insert(addPharmacyMedicineDTO.getPharmacyId(), addPharmacyMedicineDTO.getMedicineId());

        return new ResponseEntity(HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/{pid}/medicine/{mid}")
    public ResponseEntity deletePharmacyMedicine(@PathVariable UUID pid, @PathVariable UUID mid, @RequestHeader("Authorization") String token) {
        UUID phadminId = tokenUtils.getUserIdFromToken(token.split(" ")[1]);
        PharmacyDTO pharmacyDTO = pharmacyAdminService.getPharmacyAdminPharmacyId(phadminId);
        if(!pharmacyDTO.getId().equals(pid))
            return new ResponseEntity(HttpStatus.METHOD_NOT_ALLOWED);
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
