package team18.pharmacyapp.service.interfaces;

import team18.pharmacyapp.model.Pharmacy;
import team18.pharmacyapp.model.dtos.PharmacyAdminDTO;
import team18.pharmacyapp.model.dtos.PharmacyDTO;
import team18.pharmacyapp.model.dtos.UserInfoDTO;
import team18.pharmacyapp.model.users.PharmacyAdmin;

import java.util.UUID;

public interface PharmacyAdminService {

    PharmacyAdmin getById(UUID id);

    UserInfoDTO getInfoById(UUID id);

    UserInfoDTO update(PharmacyAdmin pharmacyAdmin);

    PharmacyAdmin registerNewPharmacyAdmin(PharmacyAdminDTO pharmacyAdmin);

    PharmacyDTO getPharmacyAdminPharmacyId(UUID id);
}
