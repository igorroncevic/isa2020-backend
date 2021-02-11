package team18.pharmacyapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import team18.pharmacyapp.model.dtos.EPrescriptionDTO;
import team18.pharmacyapp.model.dtos.EPrescriptionSortFilterDTO;
import team18.pharmacyapp.model.exceptions.ActionNotAllowedException;
import team18.pharmacyapp.service.interfaces.EPrescriptionService;

import java.util.List;

@CrossOrigin(origins = {"http://localhost:8080", "http://localhost:8081"})
@RestController
@RequestMapping(value = "api/eprescriptions")
public class EPrescriptionController {
    private final EPrescriptionService ePrescriptionService;

    @Autowired
    public EPrescriptionController(EPrescriptionService ePrescriptionService) {
        this.ePrescriptionService = ePrescriptionService;
    }

    @PreAuthorize("hasRole('ROLE_PATIENT')")
    @PostMapping("/patient/")
    public ResponseEntity<List<EPrescriptionDTO>> findAllByPatientId(@RequestBody EPrescriptionSortFilterDTO esf) {
        List<EPrescriptionDTO> prescriptions;
        try {
            prescriptions = ePrescriptionService.findAllByPatientId(esf);
        } catch (ActionNotAllowedException ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (RuntimeException ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(prescriptions, HttpStatus.OK);
    }
}
