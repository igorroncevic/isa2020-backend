package team18.pharmacyapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team18.pharmacyapp.model.dtos.NewPurchaseOrderDTO;
import team18.pharmacyapp.model.dtos.PurchaseOrderDTO;
import team18.pharmacyapp.model.dtos.PurchaseOrderOfferDTO;
import team18.pharmacyapp.model.exceptions.FailedToSaveException;
import team18.pharmacyapp.service.interfaces.PurchaseOrderService;

import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = {"http://localhost:8080", "http://localhost:8081"})
@RestController
@RequestMapping(value = "api/orders")
public class PurchaseOrderController {

    private final PurchaseOrderService purchaseOrderService;

    @Autowired
    public PurchaseOrderController(PurchaseOrderService purchaseOrderService) {
        this.purchaseOrderService = purchaseOrderService;
    }

    @PostMapping
    public ResponseEntity addNewPurchaseOrder(@RequestBody NewPurchaseOrderDTO newPurchaseOrderDTO) {
        PurchaseOrderDTO purchaseOrderDTO = null;
        try {
            purchaseOrderDTO = purchaseOrderService.addPurchaseOrder(newPurchaseOrderDTO);
        } catch (FailedToSaveException e) {
            return new ResponseEntity(null, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(purchaseOrderDTO, HttpStatus.OK);
    }

    @GetMapping("/{id}/offers")
    public ResponseEntity<List<PurchaseOrderOfferDTO>> getAllOffersForOrder(@PathVariable UUID id) {
        List<PurchaseOrderOfferDTO> purchaseOrderOfferDTOS = purchaseOrderService.getAllOffersForOrder(id);
        return new ResponseEntity<>(purchaseOrderOfferDTOS, HttpStatus.OK);
    }
}
