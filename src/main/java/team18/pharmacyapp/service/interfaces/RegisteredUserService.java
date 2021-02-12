package team18.pharmacyapp.service.interfaces;

import team18.pharmacyapp.model.dtos.RegisterUserDTO;
import team18.pharmacyapp.model.dtos.security.ChangePassDTO;
import team18.pharmacyapp.model.enums.UserRole;
import team18.pharmacyapp.model.users.RegisteredUser;

import java.util.List;
import java.util.UUID;

public interface RegisteredUserService {
    RegisteredUser findById(UUID id);

    RegisteredUser findByEmail(String email);

    List<RegisteredUser> findAll();

    RegisteredUser save(RegisterUserDTO user, UserRole role, String user_role);

    boolean changeFirstPass(ChangePassDTO dto);
}
