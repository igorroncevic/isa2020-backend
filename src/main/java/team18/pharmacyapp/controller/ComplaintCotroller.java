package team18.pharmacyapp.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team18.pharmacyapp.model.dtos.ComplaintDTO;
import team18.pharmacyapp.service.interfaces.ComplaintService;

import java.util.List;

@RestController
@RequestMapping(value = "api/complaints")
public class ComplaintCotroller {

    private final ComplaintService complaintService;

    @Autowired
    public ComplaintCotroller(ComplaintService complaintService) {
        this.complaintService = complaintService;
    }

    @GetMapping
    public ResponseEntity<List<ComplaintDTO>> allComplaints(){
        return new ResponseEntity<>(complaintService.getAllComplaints(), HttpStatus.OK);
    }

    @PostMapping("{type}")
    public int postComplaint(@RequestBody  ComplaintDTO dto, @PathVariable String type){
        return complaintService.saveComplaint(dto,type);
    }

    @PutMapping("response")
    public ResponseEntity<Boolean> postResponse(@RequestBody ComplaintDTO dto){
        if(complaintService.response(dto)){
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
