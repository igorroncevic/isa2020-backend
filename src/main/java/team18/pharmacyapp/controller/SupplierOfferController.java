package team18.pharmacyapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import team18.pharmacyapp.model.dtos.SupplierPurchaseOrderDTO;
import team18.pharmacyapp.service.interfaces.PurchaseOrderService;
import team18.pharmacyapp.service.interfaces.SupplierOfferService;

import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping(value = "/api/offers", produces = MediaType.APPLICATION_JSON_VALUE)
public class SupplierOfferController {
    private final SupplierOfferService supplierOfferService;

    @Autowired
    public SupplierOfferController(SupplierOfferService supplierOfferService) {
        this.supplierOfferService = supplierOfferService;
    }

    @PreAuthorize("hasRole('ROLE_SUPPLIER')")
    @GetMapping("bla/supplier/{id}")
    List<SupplierPurchaseOrderDTO> getSuppliersPO(@PathVariable UUID id){
        return supplierOfferService.getSupplierPurchaseOrders(id);
    }

    @GetMapping
    List<SupplierPurchaseOrderDTO> getAll(){
        return supplierOfferService.getAll();
    }

    @PreAuthorize("hasRole('ROLE_SUPPLIER')")
    @PostMapping("/offer")
    public ResponseEntity<Boolean> giveOffer(@RequestBody SupplierPurchaseOrderDTO orderDTO){
        return new ResponseEntity<>(supplierOfferService.givePurchaseOffer(orderDTO), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ROLE_SUPPLIER')")
    @PutMapping(consumes = "application/json")
    public ResponseEntity<SupplierPurchaseOrderDTO> update(@RequestBody SupplierPurchaseOrderDTO orderDTO) {
        if(supplierOfferService.update(orderDTO)){
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}