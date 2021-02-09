package team18.pharmacyapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import team18.pharmacyapp.model.Promotion;
import team18.pharmacyapp.service.interfaces.PromotionService;

import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = {"http://localhost:8080","http://localhost:8081"})
@RestController
@RequestMapping(value = "api/promotions")
public class PromotionController {

    private final PromotionService promotionService;

    @Autowired
    public PromotionController(PromotionService promotionService) {
        this.promotionService = promotionService;
    }

    @GetMapping("/pharmacy/{id}")
    public ResponseEntity<List<Promotion>> getAllPromotionsForPharmacy(@PathVariable UUID id) {
        List<Promotion> promotions = promotionService.getAllPromotionsForPharmacy(id);
        return new ResponseEntity<>(promotions, HttpStatus.OK);
    }
}
