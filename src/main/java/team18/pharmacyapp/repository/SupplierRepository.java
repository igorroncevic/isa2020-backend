package team18.pharmacyapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team18.pharmacyapp.model.users.Supplier;

import java.util.UUID;

public interface SupplierRepository extends JpaRepository<Supplier, UUID> {
}
