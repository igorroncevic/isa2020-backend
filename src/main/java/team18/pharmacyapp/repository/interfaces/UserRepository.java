package team18.pharmacyapp.repository.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import team18.pharmacyapp.model.users.User;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
}
