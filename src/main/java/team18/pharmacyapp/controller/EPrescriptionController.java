package team18.pharmacyapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team18.pharmacyapp.model.dtos.EPrescriptionDTO;
import team18.pharmacyapp.model.exceptions.ActionNotAllowedException;
import team18.pharmacyapp.model.medicine.EPrescription;
import team18.pharmacyapp.service.interfaces.EPrescriptionService;

import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = {"http://localhost:8080","http://localhost:8081"})
@RestController
@RequestMapping(value = "api/eprescriptions")
public class EPrescriptionController {
    private final EPrescriptionService ePrescriptionService;

    @Autowired
    public EPrescriptionController(EPrescriptionService ePrescriptionService) {
        this.ePrescriptionService = ePrescriptionService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<EPrescriptionDTO>> findAllByPatientId(@PathVariable UUID id) {
        List<EPrescriptionDTO> prescriptions;
        try{
            prescriptions = ePrescriptionService.findAllByPatientId(id);
        }catch(ActionNotAllowedException ex){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }catch(RuntimeException ex){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(prescriptions, HttpStatus.OK);
    }
}
