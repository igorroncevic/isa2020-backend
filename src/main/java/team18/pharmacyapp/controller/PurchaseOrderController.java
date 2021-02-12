package team18.pharmacyapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import team18.pharmacyapp.model.SupplierPurchaseOrder;
import team18.pharmacyapp.model.dtos.SupplierPurchaseOrderDTO;
import team18.pharmacyapp.model.users.Doctor;
import team18.pharmacyapp.service.interfaces.PurchaseOrderService;

import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping(value = "/api/purchaseorders", produces = MediaType.APPLICATION_JSON_VALUE)
public class PurchaseOrderController {
    private final PurchaseOrderService purchaseOrderService;

    @Autowired
    public PurchaseOrderController(PurchaseOrderService purchaseOrderService) {
        this.purchaseOrderService = purchaseOrderService;
    }

    @PreAuthorize("hasRole('ROLE_SUPPLIER')")
    @GetMapping("bla/supplier/{id}")
    List<SupplierPurchaseOrderDTO> getSuppliersPO(@PathVariable UUID id){
        return purchaseOrderService.getSupplierPurchaseOrders(id);
    }

    @GetMapping
    List<SupplierPurchaseOrderDTO> getAll(){
        return purchaseOrderService.getAll();
    }

    @PreAuthorize("hasRole('ROLE_SUPPLIER')")
    @PostMapping("/offer")
    public ResponseEntity<Boolean> giveOffer(@RequestBody SupplierPurchaseOrderDTO orderDTO){
        return new ResponseEntity<>(purchaseOrderService.givePurchaseOffer(orderDTO), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ROLE_SUPPLIER')")
    @PutMapping(consumes = "application/json")
    public ResponseEntity<SupplierPurchaseOrderDTO> update(@RequestBody SupplierPurchaseOrderDTO orderDTO) {
        if(purchaseOrderService.update(orderDTO)){
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
