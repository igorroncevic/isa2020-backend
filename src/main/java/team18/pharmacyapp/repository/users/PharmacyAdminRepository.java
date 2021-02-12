package team18.pharmacyapp.repository.users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import team18.pharmacyapp.model.Pharmacy;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import team18.pharmacyapp.model.users.PharmacyAdmin;

import java.util.UUID;

public interface PharmacyAdminRepository extends JpaRepository<PharmacyAdmin, UUID> {

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE pharmacy_admin SET id=:newId " +
            "WHERE id = :phAdminId")
    int setId(UUID phAdminId,UUID newId);

}
