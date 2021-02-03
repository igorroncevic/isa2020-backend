package team18.pharmacyapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team18.pharmacyapp.model.Pricings;
import team18.pharmacyapp.model.Vacation;
import team18.pharmacyapp.model.dtos.PricingsDTO;
import team18.pharmacyapp.model.enums.VacationStatus;
import team18.pharmacyapp.service.interfaces.PricingsService;

import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = {"http://localhost:8080","http://localhost:8081"})
@RestController
@RequestMapping(value = "api/pricings")
public class PricingsController {

    private final PricingsService pricingsService;

    @Autowired
    public PricingsController(PricingsService pricingsService) {
        this.pricingsService = pricingsService;
    }

    @GetMapping("/pharmacy/{id}")
    public ResponseEntity<List<PricingsDTO>> getAllCurrentPricingsForPharmacy(@PathVariable UUID id) {
        List<PricingsDTO> pricings = pricingsService.getAllCurrentPricingsForPhramcy(id);
        return new ResponseEntity<>(pricings, HttpStatus.OK);
    }


}
