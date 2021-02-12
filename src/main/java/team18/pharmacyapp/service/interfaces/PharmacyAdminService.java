package team18.pharmacyapp.service.interfaces;

import team18.pharmacyapp.model.Pharmacy;
import team18.pharmacyapp.model.dtos.PharmacyAdminDTO;
import team18.pharmacyapp.model.dtos.PharmacyDTO;
import team18.pharmacyapp.model.dtos.UserInfoDTO;
import team18.pharmacyapp.model.users.PharmacyAdmin;
import team18.pharmacyapp.model.users.RegisteredUser;

import java.util.UUID;

public interface PharmacyAdminService {

    PharmacyAdmin getById(UUID id);

    UserInfoDTO getInfoById(UUID id);

    UserInfoDTO update(PharmacyAdmin pharmacyAdmin);

    PharmacyDTO getPharmacyAdminPharmacyId(UUID id);

    PharmacyAdmin registerPharmacyAdmin(RegisteredUser user, String pharmacyName);

}
