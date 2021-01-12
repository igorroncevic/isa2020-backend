package team18.pharmacyapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team18.pharmacyapp.model.users.User;
import team18.pharmacyapp.service.UserService;

import java.util.List;

@RequestMapping("users")
@RestController
@CrossOrigin("*")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getAll() {
        return userService.findAll();
    }

    @PostMapping
    public void newUser() {
        userService.addUser();
    }

}
