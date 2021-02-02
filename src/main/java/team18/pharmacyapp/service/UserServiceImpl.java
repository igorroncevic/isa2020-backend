package team18.pharmacyapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team18.pharmacyapp.model.users.RegisteredUser;
import team18.pharmacyapp.repository.UserRepository;
import team18.pharmacyapp.service.interfaces.UserService;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) { this.userRepository = userRepository; }

    public List<RegisteredUser> findAll() { return userRepository.findAll(); }

}
