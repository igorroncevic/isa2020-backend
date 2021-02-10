package team18.pharmacyapp.repository.users;

import org.springframework.data.jpa.repository.JpaRepository;
import team18.pharmacyapp.model.users.SystemAdmin;

import java.util.UUID;

public interface SystemAdminRepository extends JpaRepository<SystemAdmin, UUID> {
}
