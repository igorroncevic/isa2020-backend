package team18.pharmacyapp.service.interfaces;

import team18.pharmacyapp.model.dtos.RegisterUserDTO;
import team18.pharmacyapp.model.users.RegisteredUser;
import team18.pharmacyapp.model.users.SystemAdmin;

import java.util.List;

public interface SystemAdminService {
    List<SystemAdmin> getAll();

    SystemAdmin registerSysAdmin(RegisteredUser user);
}
