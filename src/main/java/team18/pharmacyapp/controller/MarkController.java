package team18.pharmacyapp.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import team18.pharmacyapp.model.dtos.MarkDTO;
import team18.pharmacyapp.model.exceptions.ActionNotAllowedException;
import team18.pharmacyapp.model.exceptions.AlreadyGivenMarkException;
import team18.pharmacyapp.service.interfaces.MarkService;

@CrossOrigin(origins = {"http://localhost:8080","http://localhost:8081"})
@RestController
@RequestMapping(value = "api/marks")
public class MarkController {
    private final MarkService markService;

    @Autowired
    public MarkController(MarkService markService) {
        this.markService = markService;
    }

    @PreAuthorize("hasRole('ROLE_PATIENT')")
    @PostMapping(consumes = "application/json")
    public ResponseEntity<Void> giveMark(@RequestBody MarkDTO markDTO) {
        boolean success;

        try{
            success = markService.giveMark(markDTO);
        }catch(ActionNotAllowedException e){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }catch(AlreadyGivenMarkException e){
            return new ResponseEntity<>(HttpStatus.ALREADY_REPORTED);
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
    @PutMapping(consumes = "application/json")
    public ResponseEntity<Void> updateMark(@RequestBody MarkDTO markDTO) {
        boolean success;

        try{
            success = markService.updateMark(markDTO);
        }catch(ActionNotAllowedException e){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }catch(RuntimeException e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if(success){
            return new ResponseEntity<>(HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
