package team18.pharmacyapp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import team18.pharmacyapp.model.Promotion;
import team18.pharmacyapp.model.dtos.PromotionDTO;
import team18.pharmacyapp.service.interfaces.PromotionService;

import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = {"http://localhost:8080","http://localhost:8081"})
@RestController
@RequestMapping(value = "api/promotions")
public class PromotionController {
    private final PromotionService promotionService;

    public PromotionController(PromotionService promotionService) {
        this.promotionService = promotionService;
    }

    @PreAuthorize("hasRole('ROLE_PATIENT')")
    @GetMapping("patient/{id}")
    public ResponseEntity<List<PromotionDTO>> getPatientsPromotions(@PathVariable UUID id){
        List<PromotionDTO> promotions;
        try{
            promotions = promotionService.getPatientsPromotions(id);
        }catch (RuntimeException e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(promotions, HttpStatus.OK);
    }
}
