package team18.pharmacyapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import team18.pharmacyapp.model.Pharmacy;
import team18.pharmacyapp.model.enums.UserRole;
import team18.pharmacyapp.model.users.Doctor;

import java.util.List;
import java.util.UUID;

public interface DoctorRepository extends JpaRepository<Doctor, UUID> {

    @Query("SELECT d FROM doctor d WHERE d.role = :doctorRole")
    List<Doctor> findAllDoctors(@Param("doctorRole") UserRole doctorRole);

    @Query("SELECT ws.pharmacy FROM work_schedule ws WHERE ws.doctor.id = :doctorId")
    List<Pharmacy> findAllDoctorsPharmacies(@Param("doctorId") UUID doctorId);

    @Query("SELECT AVG(m.mark) FROM mark m WHERE m.doctor.id = :doctorId")
    Float getAverageMarkForDoctor(@Param("doctorId") UUID doctorId);

    @Query("SELECT d FROM doctor d " +
            "JOIN FETCH d.workSchedules ws " +
            "JOIN ws.pharmacy p " +
            "WHERE p.id = :pharmacyId AND d.role = 'pharmacist'")
    List<Doctor> findAllPharmacistsInPharmacy(@Param("pharmacyId") UUID pharmacyId);
}
