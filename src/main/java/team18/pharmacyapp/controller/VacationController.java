package team18.pharmacyapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team18.pharmacyapp.model.Vacation;
import team18.pharmacyapp.service.interfaces.VacationService;

import java.util.List;

@CrossOrigin(origins = {"http://localhost:8080","http://localhost:8081"})
@RestController
@RequestMapping(value = "api/vacation")
public class VacationController {

    private final VacationService vacationService;

    @Autowired
    public VacationController(VacationService vacationService) {
        this.vacationService = vacationService;
    }

    @GetMapping("/pending")
    public ResponseEntity<List<Vacation>> getAllPending() {
        List<Vacation> pendingVacations = vacationService.getAllPending();
        return new ResponseEntity<>(pendingVacations, HttpStatus.OK);
    }


}
