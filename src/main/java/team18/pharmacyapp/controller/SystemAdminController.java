package team18.pharmacyapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team18.pharmacyapp.service.interfaces.SystemAdminService;

@CrossOrigin(origins = {"http://localhost:8080", "http://localhost:8081"})
@RestController
@RequestMapping(value = "api/sysadmins")
public class SystemAdminController {
    private final SystemAdminService systemAdminService;

    @Autowired
    public SystemAdminController(SystemAdminService systemAdminService) {
        this.systemAdminService = systemAdminService;
    }
}
