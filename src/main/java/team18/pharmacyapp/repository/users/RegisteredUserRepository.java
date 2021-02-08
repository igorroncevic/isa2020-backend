package team18.pharmacyapp.repository.users;

import org.springframework.data.jpa.repository.JpaRepository;
import team18.pharmacyapp.model.users.RegisteredUser;

import java.util.UUID;

public interface RegisteredUserRepository extends JpaRepository<RegisteredUser, UUID> {
    RegisteredUser findByEmail(String email);
}
