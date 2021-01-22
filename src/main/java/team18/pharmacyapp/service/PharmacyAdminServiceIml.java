package team18.pharmacyapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team18.pharmacyapp.model.users.PharmacyAdmin;
import team18.pharmacyapp.repository.PharmacyAdminRepository;
import team18.pharmacyapp.repository.UserRepository;
import team18.pharmacyapp.service.interfaces.PharmacyAdminService;

import java.util.UUID;

@Service
public class PharmacyAdminServiceIml implements PharmacyAdminService {
    private final PharmacyAdminRepository pharmacyAdminRepository;

    @Autowired
    public PharmacyAdminServiceIml(PharmacyAdminRepository pharmacyAdminRepository) {
        this.pharmacyAdminRepository = pharmacyAdminRepository;
    }


    @Override
    public PharmacyAdmin getById(UUID id) {
        return pharmacyAdminRepository.findById(id).orElse(null);
    }

    @Override
    public PharmacyAdmin update(PharmacyAdmin pharmacyAdmin) {
        return pharmacyAdminRepository.save(pharmacyAdmin);
    }
}
