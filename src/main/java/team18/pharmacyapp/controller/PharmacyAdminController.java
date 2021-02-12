package team18.pharmacyapp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team18.pharmacyapp.model.Pharmacy;
import team18.pharmacyapp.model.dtos.PharmacyAdminDTO;
import team18.pharmacyapp.model.dtos.PharmacyDTO;
import team18.pharmacyapp.model.dtos.UserInfoDTO;
import team18.pharmacyapp.model.users.PharmacyAdmin;
import team18.pharmacyapp.service.interfaces.PharmacyAdminService;

import java.util.UUID;

@CrossOrigin(origins = {"http://localhost:8080", "http://localhost:8081"})
@RestController
@RequestMapping(value = "api/phadmin")
public class PharmacyAdminController {
    private final PharmacyAdminService pharmacyAdminService;

    public PharmacyAdminController(PharmacyAdminService pharmacyAdminService) {
        this.pharmacyAdminService = pharmacyAdminService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserInfoDTO> getById(@PathVariable UUID id) {
        UserInfoDTO pharmacyAdmin = pharmacyAdminService.getInfoById(id);
        return new ResponseEntity<>(pharmacyAdmin, HttpStatus.OK);
    }

    @PutMapping(consumes = "application/json")
    public ResponseEntity<UserInfoDTO> update(@RequestBody UserInfoDTO pharmacyAdmin) {
        PharmacyAdmin pharmacyAdminForUpdate = pharmacyAdminService.getById(pharmacyAdmin.getId());

        if (pharmacyAdmin == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        pharmacyAdminForUpdate.setName(pharmacyAdmin.getName());
        pharmacyAdminForUpdate.setSurname(pharmacyAdmin.getSurname());
        pharmacyAdminForUpdate.setEmail(pharmacyAdmin.getEmail());

        pharmacyAdmin = pharmacyAdminService.update(pharmacyAdminForUpdate);
        return new ResponseEntity<>(pharmacyAdmin, HttpStatus.OK);
    }

    @GetMapping("/{id}/pharmacy")
    public ResponseEntity<PharmacyDTO> getPharmacyAdminsPharmacyId(@PathVariable UUID id) {
        PharmacyDTO pharmacy = pharmacyAdminService.getPharmacyAdminPharmacyId(id);
        return new ResponseEntity<>(pharmacy, HttpStatus.OK);
    }
}
