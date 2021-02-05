package team18.pharmacyapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team18.pharmacyapp.model.medicine.MedicineSpecification;

import java.util.UUID;

public interface MedicineSpecificationRepository extends JpaRepository<MedicineSpecification, UUID> {
}
