package team18.pharmacyapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team18.pharmacyapp.model.Pharmacy;
import team18.pharmacyapp.model.dtos.PharmacyAdminDTO;
import team18.pharmacyapp.model.dtos.PharmacyDTO;
import team18.pharmacyapp.model.dtos.UserInfoDTO;
import team18.pharmacyapp.model.enums.UserRole;
import team18.pharmacyapp.model.users.PharmacyAdmin;
import team18.pharmacyapp.model.users.RegisteredUser;
import team18.pharmacyapp.repository.users.PharmacyAdminRepository;
import team18.pharmacyapp.repository.PharmacyRepository;
import team18.pharmacyapp.repository.users.PharmacyAdminRepository;
import team18.pharmacyapp.service.interfaces.PharmacyAdminService;

import java.util.UUID;

@Service
public class PharmacyAdminServiceIml implements PharmacyAdminService {
    private final PharmacyAdminRepository pharmacyAdminRepository;
    private final PharmacyRepository pharmacyRepository;

    @Autowired
    public PharmacyAdminServiceIml(PharmacyAdminRepository pharmacyAdminRepository, PharmacyRepository pharmacyRepository) {
        this.pharmacyAdminRepository = pharmacyAdminRepository;
        this.pharmacyRepository = pharmacyRepository;
    }

    public PharmacyAdmin getById(UUID id) {
        return pharmacyAdminRepository.findById(id).orElse(null);
    }

    @Override
    public UserInfoDTO getInfoById(UUID id) {
        PharmacyAdmin pharmacyAdmin = pharmacyAdminRepository.findById(id).orElse(null);
        UserInfoDTO pharmacyAdminInfoDTO = new UserInfoDTO(pharmacyAdmin.getId(), pharmacyAdmin.getName(),
                pharmacyAdmin.getSurname(), pharmacyAdmin.getEmail(), pharmacyAdmin.getPhoneNumber());
        return pharmacyAdminInfoDTO;
    }

    @Override
    public UserInfoDTO update(PharmacyAdmin pharmacyAdmin) {
        pharmacyAdmin = pharmacyAdminRepository.save(pharmacyAdmin);
        UserInfoDTO pharmacyAdminInfoDTO = new UserInfoDTO(pharmacyAdmin.getId(), pharmacyAdmin.getName(),
                pharmacyAdmin.getSurname(), pharmacyAdmin.getEmail(), pharmacyAdmin.getPhoneNumber());
        return pharmacyAdminInfoDTO;
    }



    @Override
    public PharmacyAdmin registerPharmacyAdmin(RegisteredUser user, String pharmacyName) {
        PharmacyAdmin phAdmin = new PharmacyAdmin();
        Pharmacy pharmacy = pharmacyRepository.findByName(pharmacyName);
        phAdmin.setRole(UserRole.pharmacyAdmin);
        phAdmin.setName(user.getName());
        phAdmin.setSurname(user.getSurname());
        phAdmin.setPhoneNumber(user.getPhoneNumber());
        phAdmin.setEmail(user.getEmail());
        phAdmin.setPassword(user.getPassword());
        phAdmin.setAddress(user.getAddress());
        phAdmin.setPharmacy(pharmacy);
        phAdmin= pharmacyAdminRepository.save(phAdmin);
        pharmacyAdminRepository.setId(phAdmin.getId(), user.getId());
        return phAdmin;
    }

    @Override
    public PharmacyDTO getPharmacyAdminPharmacyId(UUID id) {
        String pharmacyIdStr = pharmacyRepository.getPharmacyAdminsPharmacyId(id);
        UUID pharmacyId = UUID.fromString(pharmacyIdStr);
        Pharmacy pharmacy = pharmacyRepository.findById(pharmacyId).orElse(null);

        PharmacyDTO pharmacyDTO = new PharmacyDTO();
        pharmacyDTO.setId(pharmacyId);
        pharmacyDTO.setName(pharmacy.getName());
        pharmacyDTO.setCity(pharmacy.getAddress().getCity());
        pharmacyDTO.setCountry(pharmacy.getAddress().getCountry());
        pharmacyDTO.setStreet(pharmacy.getAddress().getStreet());
        return pharmacyDTO;
    }
}
