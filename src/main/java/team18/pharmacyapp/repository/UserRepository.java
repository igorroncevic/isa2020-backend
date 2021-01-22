package team18.pharmacyapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import team18.pharmacyapp.model.medicine.PharmacyMedicines;
import team18.pharmacyapp.model.users.RegisteredUser;

import java.util.List;
import java.util.UUID;

public interface UserRepository extends JpaRepository<RegisteredUser, UUID> {

    @Transactional(readOnly = true)
    @Query(value = "SELECT u FROM registered_user u WHERE u.id = :id")
    RegisteredUser getById(@Param("id") UUID id);

}
