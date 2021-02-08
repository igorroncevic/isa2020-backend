package team18.pharmacyapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team18.pharmacyapp.model.dtos.RegisterUserDTO;
import team18.pharmacyapp.model.users.Supplier;
import team18.pharmacyapp.service.interfaces.SupplierService;

@CrossOrigin(origins = {"http://localhost:8080","http://localhost:8081"})
@RestController
@RequestMapping(value = "api/suppliers")
public class SupplierController {
    private final SupplierService supplierService;

    @Autowired
    public SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    @PostMapping(consumes = "application/json", value = "/register")
    public ResponseEntity<Supplier> registerNewSupplier(@RequestBody RegisterUserDTO newSupplier){
        Supplier supp = supplierService.registerNewSupplier(newSupplier);
        return new ResponseEntity<>(supp, HttpStatus.CREATED);

    }
}
