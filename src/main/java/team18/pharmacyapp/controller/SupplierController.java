package team18.pharmacyapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import team18.pharmacyapp.model.dtos.UpdateMyDataDTO;
import team18.pharmacyapp.model.users.Supplier;
import team18.pharmacyapp.service.interfaces.SupplierService;

import java.util.UUID;

@CrossOrigin(origins = {"http://localhost:8080","http://localhost:8081"})
@RestController
@RequestMapping(value = "api/suppliers")
public class SupplierController {
    private final SupplierService supplierService;

    @Autowired
    public SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    @PreAuthorize("hasRole('ROLE_SUPPLIER')")
    @GetMapping("/{id}")
    public ResponseEntity<Supplier> getById(@PathVariable UUID id) {
        Supplier supplier = supplierService.getById(id);
        return new ResponseEntity<>(supplier, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_SUPPLIER')")
    @GetMapping("/profile/{id}")
    public ResponseEntity<UpdateMyDataDTO> getSuppById(@PathVariable UUID id) {
        UpdateMyDataDTO user = supplierService.getSupplierById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_SUPPLIER')")
    @PutMapping(consumes = "application/json")
    public ResponseEntity<Boolean> update(@RequestBody UpdateMyDataDTO data) {
        boolean updated = supplierService.update(data);
        if(updated){
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }
}
