package team18.pharmacyapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import team18.pharmacyapp.model.Pharmacy;
import team18.pharmacyapp.model.dtos.DoctorsPatientDTO;
import team18.pharmacyapp.model.dtos.ReservedMedicineResponseDTO;
import team18.pharmacyapp.model.enums.UserRole;
import team18.pharmacyapp.model.users.Doctor;

import java.util.List;
import java.util.UUID;

public interface DoctorRepository extends JpaRepository<Doctor, UUID> {

    @Query("SELECT d FROM doctor d WHERE d.role = :doctorRole")
    List<Doctor> findAllDoctors(@Param("doctorRole") UserRole doctorRole);

    @Query("SELECT ws.pharmacy.name FROM work_schedule ws WHERE ws.doctor.id = :doctorId")
    public List<String> findAllDoctorsPharmaciesNames(@Param("doctorId") UUID doctorId);

    @Query("SELECT ws.pharmacy FROM work_schedule ws WHERE ws.doctor.id = :doctorId")
    List<Pharmacy> findAllDoctorsPharmacies(@Param("doctorId") UUID doctorId);

    @Query("SELECT COALESCE(AVG(m.mark), 0) FROM mark m WHERE m.doctor.id = :doctorId")
    Float getAverageMarkForDoctor(@Param("doctorId") UUID doctorId);

    @Query("SELECT d FROM doctor d " +
            "JOIN FETCH d.workSchedules ws " +
            "JOIN ws.pharmacy p " +
            "WHERE p.id = :pharmacyId AND d.role = 'pharmacist'")
    List<Doctor> findAllPharmacistsInPharmacy(@Param("pharmacyId") UUID pharmacyId);

    @Transactional(readOnly = true)
    @Query(value = "select distinct new team18.pharmacyapp.model.dtos.DoctorsPatientDTO(p.name,p.surname,p.email,p.phoneNumber) " +
            "from term t inner join Patient p on t.patient.id=p.id where t.doctor.id=:doctorId")
    List<DoctorsPatientDTO> findDoctorPatients(UUID doctorId);
}
