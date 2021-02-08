package team18.pharmacyapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team18.pharmacyapp.model.Loyalty;
import team18.pharmacyapp.model.dtos.LoyaltyDTO;
import team18.pharmacyapp.service.interfaces.LoyaltyService;

import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = {"http://localhost:8080","http://localhost:8081"})
@RestController
@RequestMapping(value = "api/loyalty")
public class LoyaltyController {
    private final LoyaltyService loyaltyService;

    @Autowired
    public  LoyaltyController(LoyaltyService loyaltyService){
        this.loyaltyService = loyaltyService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Loyalty>> getAll(){
        return new ResponseEntity<>(loyaltyService.findAll(), HttpStatus.OK);
    }

    @PostMapping(consumes = "application/json", value = "/add")
    public ResponseEntity<Loyalty> saveNewLoyalty(@RequestBody LoyaltyDTO newLoyalty){
        Loyalty loyalty = loyaltyService.saveNewLoyalty(newLoyalty);
        return new ResponseEntity<>(loyalty, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Loyalty> getById(@PathVariable UUID id) {
        Loyalty loyalty = loyaltyService.getById(id);
        return new ResponseEntity<>(loyalty, HttpStatus.OK);
    }

    @PutMapping(consumes = "application/json")
    public ResponseEntity<Loyalty> update(@RequestBody Loyalty loyalty) {
        Loyalty loy = loyaltyService.updateLoyalty(loyalty);
        if (loy != null) {
            return new ResponseEntity<>(loy, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteLoyalty(@PathVariable UUID id) {
        Loyalty loyalty = loyaltyService.getById(id);

        if (loyalty != null) {
            loyaltyService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
