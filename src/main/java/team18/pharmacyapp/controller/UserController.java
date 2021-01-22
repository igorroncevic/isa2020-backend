package team18.pharmacyapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team18.pharmacyapp.model.users.RegisteredUser;
import team18.pharmacyapp.service.UserServiceImpl;

import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping(value = "api/users")
public class UserController {
    private final UserServiceImpl userService;

    @Autowired
    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<RegisteredUser> getById(@PathVariable UUID id) {
        RegisteredUser registeredUser = userService.getById(id);
        return new ResponseEntity<>(registeredUser, HttpStatus.OK);
    }

    @GetMapping
    public List<RegisteredUser> getAll() {
        return userService.findAll();
    }

    @PostMapping
    public void newUser() {
        userService.addUser();
    }

}
