package team18.pharmacyapp.repository.users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import team18.pharmacyapp.model.users.Supplier;

import java.util.UUID;

public interface SupplierRepository extends JpaRepository<Supplier, UUID> {

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE supplier SET id=:newId " +
            "WHERE id = :supplierId")
    int setId(UUID supplierId,UUID newId);
}
