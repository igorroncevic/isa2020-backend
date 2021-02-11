package team18.pharmacyapp.service.interfaces;

import team18.pharmacyapp.model.dtos.PharmacyAdminDTO;
import team18.pharmacyapp.model.dtos.PharmacyAdminInfoDTO;
import team18.pharmacyapp.model.users.PharmacyAdmin;

import java.util.UUID;

public interface PharmacyAdminService {

    PharmacyAdmin getById(UUID id);

    PharmacyAdminInfoDTO getInfoById(UUID id);

    PharmacyAdminInfoDTO update(PharmacyAdmin pharmacyAdmin);

    PharmacyAdmin registerNewPharmacyAdmin(PharmacyAdminDTO pharmacyAdmin);
}
