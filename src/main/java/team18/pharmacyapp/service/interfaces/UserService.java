package team18.pharmacyapp.service.interfaces;

import team18.pharmacyapp.model.users.RegisteredUser;

import java.util.List;
import java.util.UUID;

public interface UserService {

    RegisteredUser getById(UUID id);

    public void addUser();

    public List<RegisteredUser> findAll();

}
