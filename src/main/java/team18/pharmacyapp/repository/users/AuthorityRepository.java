package team18.pharmacyapp.repository.users;

import org.springframework.data.jpa.repository.JpaRepository;
import team18.pharmacyapp.model.users.Authority;

import java.util.UUID;

public interface AuthorityRepository extends JpaRepository<Authority, UUID> {
    Authority findByName(String name);
}
