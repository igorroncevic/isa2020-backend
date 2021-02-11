package team18.pharmacyapp.service;

import org.springframework.stereotype.Service;
import team18.pharmacyapp.model.enums.UserRole;
import team18.pharmacyapp.model.users.RegisteredUser;
import team18.pharmacyapp.model.users.SystemAdmin;
import team18.pharmacyapp.repository.users.SystemAdminRepository;
import team18.pharmacyapp.service.interfaces.SystemAdminService;

import java.util.List;

@Service
public class SystemAdminServiceImpl implements SystemAdminService {
    private final SystemAdminRepository systemAdminRepository;

    public SystemAdminServiceImpl(SystemAdminRepository systemAdminRepository) {
        this.systemAdminRepository = systemAdminRepository;
    }

    @Override
    public List<SystemAdmin> getAll() {
        return systemAdminRepository.findAll();
    }

    @Override
    public SystemAdmin registerSysAdmin(RegisteredUser user) {
        SystemAdmin systemAdmin = new SystemAdmin();
        systemAdmin.setRole(UserRole.sysAdmin);
        systemAdmin.setName(user.getName());
        systemAdmin.setSurname(user.getSurname());
        systemAdmin.setPhoneNumber(user.getPhoneNumber());
        systemAdmin.setEmail(user.getEmail());
        systemAdmin.setPassword(user.getPassword());
        systemAdmin.setAddress(user.getAddress());
        systemAdmin = systemAdminRepository.save(systemAdmin);
        systemAdminRepository.setId(systemAdmin.getId(), user.getId());
        return systemAdmin;
    }
}
