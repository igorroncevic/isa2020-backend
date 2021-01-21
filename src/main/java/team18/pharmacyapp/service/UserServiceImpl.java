package team18.pharmacyapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team18.pharmacyapp.model.users.PharmacyAdmin;
import team18.pharmacyapp.model.users.User;
import team18.pharmacyapp.repository.PharmacyAdminRepository;
import team18.pharmacyapp.repository.UserRepository;
import team18.pharmacyapp.service.interfaces.UserService;

import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PharmacyAdminRepository pharmacyAdminRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PharmacyAdminRepository pharmacyAdminRepository) {
        this.userRepository = userRepository;
        this.pharmacyAdminRepository = pharmacyAdminRepository;
    }

    // Need to be replaced with user repo when we fix inheritance
    public User getById(UUID id) {
        return pharmacyAdminRepository.findById(id).get();
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public void addUser() {
        User u = new User();
        u.setName("Nikola");
        userRepository.save(u);
    }
}
