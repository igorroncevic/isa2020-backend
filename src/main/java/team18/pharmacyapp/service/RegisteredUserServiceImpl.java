package team18.pharmacyapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import team18.pharmacyapp.model.Address;
import team18.pharmacyapp.model.dtos.RegisterUserDTO;
import team18.pharmacyapp.model.dtos.security.ChangePassDTO;
import team18.pharmacyapp.model.enums.UserRole;
import team18.pharmacyapp.model.users.RegisteredUser;
import team18.pharmacyapp.repository.AddressRepository;
import team18.pharmacyapp.repository.users.RegisteredUserRepository;
import team18.pharmacyapp.service.interfaces.*;

import java.util.List;
import java.util.UUID;

@Service
public class RegisteredUserServiceImpl implements RegisteredUserService {
    private final RegisteredUserRepository  userRepository;
    private final AddressRepository addressRepository;
    private final AuthorityService authorityService;
    private final PatientService patientService;
    private final DoctorService doctorService;
    private final SupplierService supplierService;
    private final SystemAdminService systemAdminService;
    private final PharmacyAdminService pharmacyAdminService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public RegisteredUserServiceImpl(RegisteredUserRepository userRepository, AddressRepository addressRepository, AuthorityService authorityService, PatientService patientService, DoctorService doctorService, SupplierService supplierService, SystemAdminService systemAdminService, PharmacyAdminService pharmacyAdminService) {
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
        this.authorityService = authorityService;
        this.patientService = patientService;
        this.doctorService = doctorService;
        this.supplierService = supplierService;
        this.systemAdminService = systemAdminService;
        this.pharmacyAdminService = pharmacyAdminService;
    }

    @Override
    public RegisteredUser findById(UUID id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public RegisteredUser findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<RegisteredUser> findAll() {
        return userRepository.findAll();
    }

    @Override
    public RegisteredUser save(RegisterUserDTO dto, UserRole role, String user_role) {
        Address address=addressRepository.findByCountryAndCityAndStreet(dto.getCountry(), dto.getCity(), dto.getStreet());
        if(address == null){
            address = new Address();
            address.setStreet(dto.getStreet());
            address.setCity(dto.getCity());
            address.setCountry(dto.getCountry());
            address = addressRepository.save(address);
        }
        RegisteredUser user = new RegisteredUser();
        user.setRole(role);
        user.setName(dto.getName());
        user.setSurname(dto.getSurname());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setAddress(address);
        user.setAuthorities(authorityService.findByName(user_role));
        userRepository.save(user);

        switch (role){
            case patient: patientService.register(user);
                break;
            case dermatologist: doctorService.registerDermatologist(user);
                break;
            case supplier: supplierService.registerSupplier(user);
                break;
            case sysAdmin: systemAdminService.registerSysAdmin(user);
                break;
            case pharmacyAdmin: pharmacyAdminService.registerPharmacyAdmin(user, dto.getPharmacy());
                break;
        }
        return user;
    }

    @Override
    public boolean changeFirstPass(ChangePassDTO dto) {
        RegisteredUser user=findByEmail(dto.getEmail());
        String newPass= passwordEncoder.encode(dto.getNewPass());
        user.setPassword(newPass);
        user.setFirstLogin(false);
        userRepository.save(user);
        return true;

    }

}
