package team18.pharmacyapp.service.interfaces;

import team18.pharmacyapp.model.users.PharmacyAdmin;
import team18.pharmacyapp.model.users.User;

import java.util.List;
import java.util.UUID;

public interface UserService {

    User getById(UUID id);

    public void addUser();

    public List<User> findAll();

}
