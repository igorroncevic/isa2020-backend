package team18.pharmacyapp.repository.users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import team18.pharmacyapp.model.medicine.Medicine;
import team18.pharmacyapp.model.users.Patient;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface PatientRepository extends JpaRepository<Patient, UUID> {

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "insert into patient (id, name, surname, email, phone_number, password, role, fk_address, loyalty_points, loyalty_id, penalties, activated)" +
            "values (:id, :name, :surname,:email, :phone, :pass, 'patient'," +
            "        :addressId, 0, :loyaltyId, 0, false );")
    int save(UUID id,String name,String surname,String email,String phone,String pass,UUID addressId,UUID loyaltyId);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE patient SET penalties = penalties + 1 " +
            "WHERE id = :patientId AND penalties < 3")
    int addPenalty(@Param("patientId") UUID patientId);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE patient SET id=:newId " +
            "WHERE id = :patientId")
    int setId(UUID patientId,UUID newId);

    @Transactional(readOnly = true)
    @Query(value = "select p.alergicMedicines from patient p where p.id=:patientId")
    List<Medicine> getAlergicMedicines(@Param("patientId") UUID patientId);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE patient SET penalties = 0 WHERE id = :patientId")
    int resetPenalties(@Param("patientId") UUID patientId);

    Patient findByEmailAndPassword(String email, String password);
    Patient findByEmail(String email);

    @Transactional(readOnly = true)
    @Query("SELECT p FROM patient p JOIN FETCH p.address JOIN FETCH p.loyalty WHERE p.id = :patientId")
    Patient getPatientForProfile(@Param("patientId")UUID patientId);
}
