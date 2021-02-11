package team18.pharmacyapp.repository.users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import team18.pharmacyapp.model.users.RegisteredUser;

import java.util.UUID;

public interface RegisteredUserRepository extends JpaRepository<RegisteredUser, UUID> {
    RegisteredUser findByEmail(String email);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE registered_user SET name=:name,surname=:surname,password=:password,phone_number = :phone " +
            "WHERE id = :patientId")
    int update(@Param("patientId") UUID patientId, @Param("name") String name, @Param("surname") String surname, @Param("password") String password, @Param("phone") String phone);
}
