package team18.pharmacyapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import team18.pharmacyapp.model.dtos.*;
import team18.pharmacyapp.model.exceptions.ActionNotAllowedException;
import team18.pharmacyapp.model.medicine.Medicine;
import team18.pharmacyapp.model.exceptions.ReserveMedicineException;
import team18.pharmacyapp.model.medicine.MedicineSpecification;
import team18.pharmacyapp.model.medicine.ReservedMedicines;
import team18.pharmacyapp.service.interfaces.MedicineService;
import team18.pharmacyapp.service.interfaces.ReservedMedicinesService;

import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = {"http://localhost:8080","http://localhost:8081"})
@RestController
@RequestMapping(value = "api/medicines")
public class MedicineController {
    private final MedicineService medicineService;
    private final ReservedMedicinesService reservedMedicinesService;

    @Autowired
    public MedicineController(MedicineService medicineService, ReservedMedicinesService reservedMedicinesService) {
        this.medicineService = medicineService;
        this.reservedMedicinesService = reservedMedicinesService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<MedicineIdNameDTO>> getAllMedicines() {
        List<MedicineIdNameDTO> medicines = medicineService.findAll();

        return new ResponseEntity<>(medicines, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_PATIENT')")
    @GetMapping
    public ResponseEntity<List<PharmacyMedicinesDTO>> getAllAvailableMedicines() {
        List<PharmacyMedicinesDTO> medicines;
        try{
            medicines = medicineService.findAllAvailableMedicines();
        }catch(RuntimeException e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(medicines, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_PATIENT')")
    @PostMapping("/filter")
    public ResponseEntity<List<MedicineFilterDTO>> filterMedicines(@RequestBody MedicineFilterRequestDTO mfr) {
        List<MedicineFilterDTO> medicines;
        try{
            medicines = medicineService.filterMedicines(mfr);
        }catch(RuntimeException e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(medicines, HttpStatus.OK);
    }

    @PostMapping("/noauth/filter")
    public ResponseEntity<List<MedicineFilterDTO>> filterNoAuthMedicines(@RequestBody MedicineFilterRequestDTO mfr) {
        List<MedicineFilterDTO> medicines;
        try{
            medicines = medicineService.filterNoAuthMedicines(mfr);
        }catch(RuntimeException e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(medicines, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_PATIENT')")
    @GetMapping("/reserved/{id}")
    public ResponseEntity<List<ReservedMedicinesDTO>> getAllPatientsReservedMedicines(@PathVariable UUID id) {
        List<ReservedMedicinesDTO> medicines;
        try{
            medicines = medicineService.findAllPatientsReservedMedicines(id);
        }catch(RuntimeException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(medicines, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_PATIENT')")
    @PostMapping("/allergy")
    public ResponseEntity<Void> addPatientsAllergy(@RequestBody MedicineAllergyDTO allergy) {
        boolean success;
        try {
            success = medicineService.addPatientsAllergy(allergy);
        }catch(RuntimeException e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if(success){
            return new ResponseEntity<>(HttpStatus.CREATED);
        }else{
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasRole('ROLE_PATIENT')")
    @GetMapping("/notallergic/{id}")
    public ResponseEntity<List<MedicineDTO>> getAllMedicinesPatientsNotAllergicTo(@PathVariable UUID id) {
        List<MedicineDTO> medicines;

        try{
            medicines = medicineService.getAllMedicinesPatientsNotAlergicTo(id);
        }catch(RuntimeException e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(medicines, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_PATIENT')")
    @GetMapping("/allergic/{id}")
    public ResponseEntity<List<MedicineDTO>> getAllMedicinesPatientsAllergicTo(@PathVariable UUID id) {
        List<MedicineDTO> medicines;

        try{
            medicines = medicineService.getAllMedicinesPatientsAllergicTo(id);
        }catch(RuntimeException e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(medicines, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_PATIENT')")
    @GetMapping("/patient/{id}")
    public ResponseEntity<List<MedicineMarkDTO>> getAllPatientsMedicinesOptimized(@PathVariable UUID id) {
        List<MedicineMarkDTO> medicines;

        try{
            medicines = medicineService.getAllMedicinesForMarkingOptimized(id);
        }catch(RuntimeException ex){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(medicines, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Medicine> getMedicine(@PathVariable UUID id) {
        Medicine medicine = medicineService.findById(id);

        if (medicine == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(medicine, HttpStatus.OK);
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity<Medicine> saveMedicine(@RequestBody Medicine medicine) {
        Medicine savedMedicine = medicineService.save(medicine);
        return new ResponseEntity<>(savedMedicine, HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteMedicine(@PathVariable UUID id) {
        Medicine medicine = medicineService.findById(id);

        if (medicine != null) {
            medicineService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasRole('ROLE_PATIENT')")
    @PostMapping(consumes = "application/json", value = "/reserve")
    public ResponseEntity<Void> reserveMedicine(@RequestBody ReserveMedicineRequestDTO medicine) {
        boolean success;
        try {
            success = medicineService.reserveMedicine(medicine);
        } catch (ActionNotAllowedException ex) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } catch (ReserveMedicineException ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (RuntimeException ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (success) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasRole('ROLE_PATIENT')")
    @PostMapping(consumes = "application/json", value = "/cancel")
    public ResponseEntity<Void> cancelMedicine(@RequestBody CancelMedicineRequestDTO medicine) {
        boolean success;
        try {
            success = medicineService.cancelMedicine(medicine);
        } catch (ReserveMedicineException ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (RuntimeException ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (success) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("reserved/{id}/{pharmacy}")
    public ResponseEntity<ReservedMedicineResponseDTO> getReservedMedicineById(@PathVariable UUID id, @PathVariable UUID pharmacy) {
        ReservedMedicineResponseDTO medicines= reservedMedicinesService.checkReservation(id,pharmacy);
        if(medicines != null) {
            return new ResponseEntity<>(medicines, HttpStatus.OK);
        }
        return  new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @PutMapping(value = "handleReservation",consumes = "application/json")
    public boolean handleReservation(@RequestBody HandleReservationDTO dto){
        return reservedMedicinesService.handleMedicine(dto);
    }

    @PostMapping(consumes = "application/json", value = "/save")
    public ResponseEntity<Medicine> saveNewMedicine(@RequestBody MedicineDTO newMedicine){
        Medicine medicine = medicineService.registerNewMedicine(newMedicine);
        return new ResponseEntity<>(medicine, HttpStatus.CREATED);
    }

    @GetMapping("specification/{id}")
    public ResponseEntity<MedicineSpecification> getSpecificatin(@PathVariable UUID id){
        MedicineSpecification specification=medicineService.getMedicineSpecification(id);
        if(specification!=null){
            return new ResponseEntity<>(specification,HttpStatus.OK);
        }
        return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }


}
