package team18.pharmacyapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import team18.pharmacyapp.model.Pharmacy;
import team18.pharmacyapp.model.users.Doctor;

import java.util.List;
import java.util.UUID;

public interface DoctorRepository extends JpaRepository<Doctor, UUID> {

    @Query("SELECT d FROM doctor d WHERE d.role = 'dermatologist'")
    List<Doctor> findAllDermatologist();

    @Query("SELECT ws.pharmacy FROM work_schedule ws WHERE ws.doctor.id = :doctorId")
    public List<Pharmacy> findAllDermatologistPharmacies(@Param("doctorId") UUID doctorId);

    @Query("SELECT AVG(m.mark) FROM mark m WHERE m.doctor.id = :doctorId")
    public Float getAverageMarkForDoctor(@Param("doctorId") UUID doctorId);

}
