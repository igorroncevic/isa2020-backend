package team18.pharmacyapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team18.pharmacyapp.model.users.RegisteredUser;
import team18.pharmacyapp.service.interfaces.RegisteredUserService;

import java.util.List;

@CrossOrigin(origins = {"http://localhost:8080","http://localhost:8081"})
@RestController
@RequestMapping(value = "api/users")
public class UserController {
    private final RegisteredUserService userService;

    @Autowired
    public UserController(RegisteredUserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<RegisteredUser> getAll() {
        return userService.findAll();
    }

}
