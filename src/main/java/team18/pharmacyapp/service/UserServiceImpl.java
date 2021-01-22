package team18.pharmacyapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team18.pharmacyapp.model.enums.UserRole;
import team18.pharmacyapp.model.users.RegisteredUser;
import team18.pharmacyapp.repository.PharmacyAdminRepository;
import team18.pharmacyapp.repository.UserRepository;
import team18.pharmacyapp.service.interfaces.UserService;

import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<RegisteredUser> findAll() {
        return userRepository.findAll();
    }

    public void addUser() {
        RegisteredUser u = new RegisteredUser();
        u.setName("Nikola");
        userRepository.save(u);
    }
}
