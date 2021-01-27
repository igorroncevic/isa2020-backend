package team18.pharmacyapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team18.pharmacyapp.model.dtos.DateTimeRangeDTO;
import team18.pharmacyapp.model.dtos.PharmacyMarkPriceDTO;
import team18.pharmacyapp.service.interfaces.CounselingService;

import java.util.List;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping(value = "api/counseling")
public class CounselingController {
    private final CounselingService counselingService;

    @Autowired
    public CounselingController(CounselingService counselingService) {
        this.counselingService = counselingService;
    }

    @GetMapping("/available")
    public ResponseEntity<List<PharmacyMarkPriceDTO>> getById(@RequestBody DateTimeRangeDTO timeRange) {
        List<PharmacyMarkPriceDTO> pharmacies = counselingService.getPharmaciesWithAvailableCounselings(timeRange);
        return new ResponseEntity<>(pharmacies, HttpStatus.OK);
    }
}
