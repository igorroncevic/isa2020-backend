package team18.pharmacyapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team18.pharmacyapp.model.Address;
import team18.pharmacyapp.model.Pharmacy;
import team18.pharmacyapp.model.dtos.PharmacyAdminDTO;
import team18.pharmacyapp.model.enums.UserRole;
import team18.pharmacyapp.model.users.PharmacyAdmin;
import team18.pharmacyapp.repository.AddressRepository;
import team18.pharmacyapp.repository.users.PharmacyAdminRepository;
import team18.pharmacyapp.repository.PharmacyRepository;
import team18.pharmacyapp.service.interfaces.PharmacyAdminService;

import java.util.UUID;

@Service
public class PharmacyAdminServiceIml implements PharmacyAdminService {
    private final PharmacyAdminRepository pharmacyAdminRepository;
    private final AddressRepository addressRepository;
    private final PharmacyRepository pharmacyRepository;

    @Autowired
    public PharmacyAdminServiceIml(PharmacyAdminRepository pharmacyAdminRepository, AddressRepository addressRepository, PharmacyRepository pharmacyRepository) {
        this.pharmacyAdminRepository = pharmacyAdminRepository;
        this.addressRepository = addressRepository;
        this.pharmacyRepository = pharmacyRepository;
    }


    @Override
    public PharmacyAdmin getById(UUID id) {
        return pharmacyAdminRepository.findById(id).orElse(null);
    }

    @Override
    public PharmacyAdmin update(PharmacyAdmin pharmacyAdmin) {
        return pharmacyAdminRepository.save(pharmacyAdmin);
    }

    @Override
    public PharmacyAdmin registerNewPharmacyAdmin(PharmacyAdminDTO pharmacyAdmin) {
        PharmacyAdmin phAdmin = new PharmacyAdmin();
        Address address = addressRepository.findByCountryAndCityAndStreet(pharmacyAdmin.getCountry(), pharmacyAdmin.getCity(), pharmacyAdmin.getStreet());
        if(address == null){
            address = new Address();
            address.setStreet(pharmacyAdmin.getStreet());
            address.setCity(pharmacyAdmin.getCity());
            address.setCountry(pharmacyAdmin.getCountry());
            address = addressRepository.save(address);
        }
        Pharmacy pharmacy = pharmacyRepository.findByName(pharmacyAdmin.getPharmacyName());

        phAdmin.setRole(UserRole.pharmacyAdmin);
        phAdmin.setName(pharmacyAdmin.getName());
        phAdmin.setSurname(pharmacyAdmin.getSurname());
        phAdmin.setPhoneNumber(pharmacyAdmin.getPhoneNumber());
        phAdmin.setEmail(pharmacyAdmin.getEmail());
        phAdmin.setPassword(pharmacyAdmin.getPassword());
        phAdmin.setAddress(address);
        phAdmin.setPharmacy(pharmacy);
        phAdmin= pharmacyAdminRepository.save(phAdmin);
        return phAdmin;
    }
}
