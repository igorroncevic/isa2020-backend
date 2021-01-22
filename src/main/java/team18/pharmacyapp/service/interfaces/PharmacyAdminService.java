package team18.pharmacyapp.service.interfaces;

import team18.pharmacyapp.model.users.PharmacyAdmin;
import team18.pharmacyapp.model.users.RegisteredUser;

import java.util.UUID;

public interface PharmacyAdminService {

    PharmacyAdmin getById(UUID id);

    PharmacyAdmin update(PharmacyAdmin pharmacyAdmin);
}
