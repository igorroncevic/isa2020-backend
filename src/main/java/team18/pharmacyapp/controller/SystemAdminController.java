package team18.pharmacyapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team18.pharmacyapp.model.dtos.RegisterUserDTO;
import team18.pharmacyapp.model.users.SystemAdmin;
import team18.pharmacyapp.service.interfaces.SystemAdminService;

@CrossOrigin(origins = {"http://localhost:8080","http://localhost:8081"})
@RestController
@RequestMapping(value = "api/sysadmins")
public class SystemAdminController {
    private final SystemAdminService systemAdminService;

    @Autowired
    public SystemAdminController(SystemAdminService systemAdminService) {
        this.systemAdminService = systemAdminService;
    }

    @PostMapping(consumes = "application/json", value = "/register")
    public ResponseEntity<SystemAdmin> registerNewSysAdmin(@RequestBody RegisterUserDTO newSysAdmin){
        SystemAdmin sysAdm = systemAdminService.registerNewSysAdmin(newSysAdmin);
        return new ResponseEntity<>(sysAdm, HttpStatus.CREATED);

    }
}
