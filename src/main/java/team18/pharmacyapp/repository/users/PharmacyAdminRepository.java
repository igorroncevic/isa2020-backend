package team18.pharmacyapp.repository.users;

import org.springframework.data.jpa.repository.JpaRepository;
import team18.pharmacyapp.model.users.PharmacyAdmin;

import java.util.UUID;

public interface PharmacyAdminRepository extends JpaRepository<PharmacyAdmin, UUID> {

}
