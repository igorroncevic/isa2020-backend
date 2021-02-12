package team18.pharmacyapp.controller;

import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import team18.pharmacyapp.model.Pricings;
import team18.pharmacyapp.model.dtos.NewPricingDTO;
import team18.pharmacyapp.model.dtos.PharmacyDTO;
import team18.pharmacyapp.model.dtos.PricingsDTO;
import team18.pharmacyapp.model.dtos.UpdatePricingDTO;
import team18.pharmacyapp.model.exceptions.ActionNotAllowedException;
import team18.pharmacyapp.model.exceptions.BadTimeRangeException;
import team18.pharmacyapp.security.TokenUtils;
import team18.pharmacyapp.service.interfaces.PharmacyAdminService;
import team18.pharmacyapp.service.interfaces.PricingsService;

import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = {"http://localhost:8080", "http://localhost:8081"})
@RestController
@RequestMapping(value = "api/pricings")
public class PricingsController {

    private final PricingsService pricingsService;
    private final TokenUtils tokenUtils;
    private final PharmacyAdminService pharmacyAdminService;

    @Autowired
    public PricingsController(PricingsService pricingsService, TokenUtils tokenUtils, PharmacyAdminService pharmacyAdminService) {
        this.pricingsService = pricingsService;
        this.tokenUtils = tokenUtils;
        this.pharmacyAdminService = pharmacyAdminService;
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

    @PreAuthorize("hasRole('ROLE_PHADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity deletePricing(@PathVariable UUID pricingId) {
        try {
            pricingsService.deletePricing(pricingId);
        } catch (NotFoundException e) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        } catch (ActionNotAllowedException e) {
            return new ResponseEntity(HttpStatus.METHOD_NOT_ALLOWED);
        }

        return new ResponseEntity(HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_PHADMIN')")
    @PostMapping
    public ResponseEntity<Pricings> addNewPricing(@RequestBody NewPricingDTO newPricingDTO, @RequestHeader("Authorization") String token) {
        UUID phadminId = tokenUtils.getUserIdFromToken(token.split(" ")[1]);
        PharmacyDTO pharmacyDTO = pharmacyAdminService.getPharmacyAdminPharmacyId(phadminId);
        newPricingDTO.setPharmacyId(pharmacyDTO.getId());
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

    @PreAuthorize("hasRole('ROLE_PHADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<String> updatePricing(@PathVariable UUID id, @RequestBody UpdatePricingDTO updatePricingDTO) {
        try {
            pricingsService.updatePricing(id, updatePricingDTO);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (ActionNotAllowedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (BadTimeRangeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("", HttpStatus.OK);
    }

}
