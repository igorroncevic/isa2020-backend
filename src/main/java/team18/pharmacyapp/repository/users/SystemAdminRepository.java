package team18.pharmacyapp.repository.users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import team18.pharmacyapp.model.users.SystemAdmin;

import java.util.UUID;

public interface SystemAdminRepository extends JpaRepository<SystemAdmin, UUID> {

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE system_admin SET id=:newId " +
            "WHERE id = :sysAdminId")
    int setId(UUID sysAdminId,UUID newId);
}
