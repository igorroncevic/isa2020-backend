package team18.pharmacyapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team18.pharmacyapp.model.Pharmacy;

import java.util.UUID;

public interface PharmacyRepository extends JpaRepository<Pharmacy, UUID> {

}
