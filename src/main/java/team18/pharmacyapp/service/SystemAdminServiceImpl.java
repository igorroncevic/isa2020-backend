package team18.pharmacyapp.service;

import org.springframework.stereotype.Service;
import team18.pharmacyapp.model.Address;
import team18.pharmacyapp.model.dtos.RegisterUserDTO;
import team18.pharmacyapp.model.enums.UserRole;
import team18.pharmacyapp.model.users.SystemAdmin;
import team18.pharmacyapp.repository.AddressRepository;
import team18.pharmacyapp.repository.SystemAdminRepository;
import team18.pharmacyapp.service.interfaces.SystemAdminService;

import java.util.List;

@Service
public class SystemAdminServiceImpl implements SystemAdminService {
    private final SystemAdminRepository systemAdminRepository;
    private final AddressRepository addressRepository;

    public SystemAdminServiceImpl(SystemAdminRepository systemAdminRepository, AddressRepository addressRepository) {
        this.systemAdminRepository = systemAdminRepository;
        this.addressRepository = addressRepository;
    }

    @Override
    public List<SystemAdmin> getAll() {
        return systemAdminRepository.findAll();
    }

    @Override
    public SystemAdmin registerNewSysAdmin(RegisterUserDTO sysAdmin) {
        SystemAdmin sysAdm = new SystemAdmin();
        Address address  =addressRepository.findByCountryAndCityAndStreet(sysAdmin.getCountry(), sysAdmin.getCity(), sysAdmin.getStreet());
        if(address == null){
            address = new Address();
            address.setStreet(sysAdmin.getStreet());
            address.setCity(sysAdmin.getCity());
            address.setCountry(sysAdmin.getCountry());
            address = addressRepository.save(address);
        }
        sysAdm.setRole(UserRole.sysAdmin);
        sysAdm.setName(sysAdmin.getName());
        sysAdm.setSurname(sysAdmin.getSurname());
        sysAdm.setPhoneNumber(sysAdmin.getPhoneNumber());
        sysAdm.setEmail(sysAdmin.getEmail());
        sysAdm.setPassword(sysAdmin.getPassword());
        sysAdm.setAddress(address);
        sysAdm = systemAdminRepository.save(sysAdm);

        return sysAdm;
    }
}
