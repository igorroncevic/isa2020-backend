package team18.pharmacyapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team18.pharmacyapp.model.Loyalty;

import java.util.UUID;

public interface LoyaltyRepository extends JpaRepository<Loyalty, UUID> {
    Loyalty findByCategory(String category);
}
