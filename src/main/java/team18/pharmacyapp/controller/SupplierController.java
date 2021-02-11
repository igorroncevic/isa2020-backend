package team18.pharmacyapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team18.pharmacyapp.model.dtos.RegisterUserDTO;
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

    @GetMapping("/{id}")
    public ResponseEntity<Supplier> getById(@PathVariable UUID id) {
        Supplier supplier = supplierService.getById(id);
        return new ResponseEntity<>(supplier, HttpStatus.OK);
    }

    @PutMapping(consumes = "application/json")
    public ResponseEntity<Supplier> update(@RequestBody UpdateMyDataDTO data) {
        Supplier supp = supplierService.update(data);
        if (supp != null) {
            return new ResponseEntity<>(supp, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
