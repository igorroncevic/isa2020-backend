package team18.pharmacyapp.controller;

import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team18.pharmacyapp.model.Pricings;
import team18.pharmacyapp.model.Vacation;
import team18.pharmacyapp.model.dtos.NewPricingDTO;
import team18.pharmacyapp.model.dtos.PricingsDTO;
import team18.pharmacyapp.model.enums.VacationStatus;
import team18.pharmacyapp.model.exceptions.ActionNotAllowedException;
import team18.pharmacyapp.model.exceptions.BadTimeRangeException;
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

    @GetMapping("/pharmacy/{phId}/medicine/{mId}")
    public ResponseEntity<List<PricingsDTO>> getAllPricingsForMedicine(@PathVariable UUID phId, @PathVariable UUID mId) {
        List<PricingsDTO> pricings = pricingsService.getAllPricingsForMedicine(phId, mId);
        return new ResponseEntity<>(pricings, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deletePricing(@PathVariable UUID id) {
        try {
            pricingsService.deletePricing(id);
        } catch (NotFoundException e) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        } catch (ActionNotAllowedException e) {
            return new ResponseEntity(HttpStatus.METHOD_NOT_ALLOWED);
        }

        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Pricings> addNewPricing(@RequestBody NewPricingDTO newPricingDTO) {

        Pricings pricings = null;
        try {
            pricings = pricingsService.addNewPricing(newPricingDTO);
        } catch (BadTimeRangeException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(pricings, HttpStatus.OK);
    }


}
