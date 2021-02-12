package team18.pharmacyapp.controller;

import io.jsonwebtoken.Jwt;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import team18.pharmacyapp.model.dtos.NewPurchaseOrderDTO;
import team18.pharmacyapp.model.dtos.PurchaseOrderDTO;
import team18.pharmacyapp.model.dtos.PurchaseOrderOfferDTO;
import team18.pharmacyapp.model.exceptions.ActionNotAllowedException;
import team18.pharmacyapp.model.exceptions.FailedToSaveException;
import team18.pharmacyapp.security.TokenUtils;
import team18.pharmacyapp.service.interfaces.PurchaseOrderService;

import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = {"http://localhost:8080", "http://localhost:8081"})
@RestController
@RequestMapping(value = "api/orders")
public class PurchaseOrderController {

    private final PurchaseOrderService purchaseOrderService;
    private final TokenUtils tokenUtils;

    @Autowired
    public PurchaseOrderController(PurchaseOrderService purchaseOrderService, TokenUtils tokenUtils) {
        this.purchaseOrderService = purchaseOrderService;
        this.tokenUtils = tokenUtils;
    }

    @PreAuthorize("hasRole('ROLE_PHADMIN')")
    @PostMapping
    public ResponseEntity<PurchaseOrderDTO> addNewPurchaseOrder(@RequestBody NewPurchaseOrderDTO newPurchaseOrderDTO, @RequestHeader("Authorization") String token) {
        UUID phadminId = tokenUtils.getUserIdFromToken(token.split(" ")[1]);
        newPurchaseOrderDTO.setPharmacyAdminId(phadminId);
        PurchaseOrderDTO purchaseOrderDTO = null;
        try {
            purchaseOrderDTO = purchaseOrderService.addPurchaseOrder(newPurchaseOrderDTO);
        } catch (FailedToSaveException e) {
            return new ResponseEntity(null, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(purchaseOrderDTO, HttpStatus.OK);
    }

    @GetMapping("/{id}/offers")
    public ResponseEntity<List<PurchaseOrderOfferDTO>> getAllOffersForOrder(@PathVariable UUID id) {
        List<PurchaseOrderOfferDTO> purchaseOrderOfferDTOS = purchaseOrderService.getAllOffersForOrder(id);
        return new ResponseEntity<>(purchaseOrderOfferDTOS, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_PHADMIN')")
    @PutMapping("/{orderId}/offers/{supplierId}/accept")
    public ResponseEntity<String> acceptOffer(@PathVariable UUID orderId, @PathVariable UUID supplierId, @RequestHeader("Authorization") String token) {
        UUID phadminId = tokenUtils.getUserIdFromToken(token.split(" ")[1]);
        try {
            purchaseOrderService.acceptOffer(orderId, supplierId, phadminId);
        } catch (ActionNotAllowedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.METHOD_NOT_ALLOWED);
        } catch (FailedToSaveException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("", HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_PHADMIN')")
    @PutMapping("/{orderId}")
    public ResponseEntity<PurchaseOrderDTO> updatePurchaseOrder(@RequestBody NewPurchaseOrderDTO newPurchaseOrderDTO, @PathVariable UUID orderId, @RequestHeader("Authorization") String token) {
        UUID phadminId = tokenUtils.getUserIdFromToken(token.split(" ")[1]);
        newPurchaseOrderDTO.setPharmacyAdminId(phadminId);
        PurchaseOrderDTO purchaseOrderDTO = null;
        try {
            purchaseOrderDTO = purchaseOrderService.updatePurchaseOrder(orderId, newPurchaseOrderDTO);
        } catch (FailedToSaveException | ActionNotAllowedException | ChangeSetPersister.NotFoundException e) {
            return new ResponseEntity(null, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(purchaseOrderDTO, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_PHADMIN')")
    @DeleteMapping("/{orderId}")
    public ResponseEntity<String> deletePurchaseOrder(@PathVariable UUID orderId, @RequestHeader("Authorization") String token) {
        UUID phadminId = tokenUtils.getUserIdFromToken(token.split(" ")[1]);
        try {
            purchaseOrderService.deletePurchaseOrder(orderId, phadminId);
        } catch (NotFoundException | ActionNotAllowedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.METHOD_NOT_ALLOWED);
        }
        return new ResponseEntity<>("", HttpStatus.OK);
    }
}
