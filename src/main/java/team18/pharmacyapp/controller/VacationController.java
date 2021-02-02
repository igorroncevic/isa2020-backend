package team18.pharmacyapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team18.pharmacyapp.model.Vacation;
import team18.pharmacyapp.model.dtos.RefuseVacationDTO;
import team18.pharmacyapp.model.enums.VacationStatus;
import team18.pharmacyapp.model.exceptions.ActionNotAllowedException;
import team18.pharmacyapp.model.exceptions.EntityNotFoundException;
import team18.pharmacyapp.service.interfaces.VacationService;

import java.util.List;
import java.util.UUID;

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
        List<Vacation> pendingVacations = vacationService.getAll(VacationStatus.pending);
        return new ResponseEntity<>(pendingVacations, HttpStatus.OK);
    }

    @GetMapping("/approved")
    public ResponseEntity<List<Vacation>> getAllApproved() {
        List<Vacation> pendingVacations = vacationService.getAll(VacationStatus.approved);
        return new ResponseEntity<>(pendingVacations, HttpStatus.OK);
    }

    @GetMapping("/refused")
    public ResponseEntity<List<Vacation>> getAllRefused() {
        List<Vacation> pendingVacations = vacationService.getAll(VacationStatus.refused);
        return new ResponseEntity<>(pendingVacations, HttpStatus.OK);
    }

    @PatchMapping(value = "/{id}/approve")
    public ResponseEntity approveVacation(@PathVariable UUID id) {
        try {
            vacationService.approve(id);
        }catch (EntityNotFoundException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (ActionNotAllowedException ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (RuntimeException ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity(HttpStatus.OK);
    }

    @PatchMapping(value = "/{id}/refuse")
    public ResponseEntity refuseVacation(@PathVariable UUID id, @RequestBody RefuseVacationDTO refuseVacationDTO) {
        try {
            vacationService.refuse(id, refuseVacationDTO.getRejectionReason());
        }catch (EntityNotFoundException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (ActionNotAllowedException ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (RuntimeException ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity(HttpStatus.OK);
    }


}