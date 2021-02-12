package team18.pharmacyapp.repository.users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import team18.pharmacyapp.model.Pharmacy;
import team18.pharmacyapp.model.dtos.DoctorsPatientDTO;
import team18.pharmacyapp.model.enums.UserRole;
import team18.pharmacyapp.model.users.Doctor;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface DoctorRepository extends JpaRepository<Doctor, UUID> {
    @Transactional(readOnly = true)
    @Query("SELECT d FROM doctor d WHERE d.role = :doctorRole")
    List<Doctor> findAllDoctors(@Param("doctorRole") UserRole doctorRole);

    @Transactional(readOnly = true)
    @Query("SELECT d FROM doctor d JOIN d.workSchedules ws WHERE ws.pharmacy.id = :pharmacyId")
    List<Doctor> findAllDoctorsInPharmacy(@Param("pharmacyId") UUID pharmacyId);

    @Transactional(readOnly = true)
    @Query("SELECT ws.pharmacy.name FROM work_schedule ws WHERE ws.doctor.id = :doctorId")
    List<String> findAllDoctorsPharmaciesNames(@Param("doctorId") UUID doctorId);

    @Transactional(readOnly = true)
    @Query("SELECT ws.pharmacy FROM work_schedule ws WHERE ws.doctor.id = :doctorId")
    List<Pharmacy> findAllDoctorsPharmacies(@Param("doctorId") UUID doctorId);

    @Transactional(readOnly = true)
    @Query("SELECT d FROM doctor d JOIN d.terms t WHERE t.patient.id = :patientId AND d.role = :doctorRole AND t.endTime < :todaysTime")
    List<Doctor> getPatientsDoctors(@Param("patientId") UUID patientId, @Param("doctorRole")UserRole doctorRole, @Param("todaysTime") Date todaysTime);

    @Transactional(readOnly = true)
    @Query("SELECT d FROM doctor d " +
            "JOIN FETCH d.workSchedules ws " +
            "JOIN ws.pharmacy p " +
            "WHERE p.id = :pharmacyId AND d.role = 'pharmacist'")
    List<Doctor> findAllPharmacistsInPharmacy(@Param("pharmacyId") UUID pharmacyId);

    @Transactional(readOnly = true)

    @Query(value = "select distinct new team18.pharmacyapp.model.dtos.DoctorsPatientDTO(p.id,p.name,p.surname,p.email,p.phoneNumber) " +
            "from term t inner join patient p on t.patient.id=p.id where t.doctor.id=:doctorId")
    List<DoctorsPatientDTO> findDoctorPatients(UUID doctorId);

    @Query("SELECT d FROM doctor d JOIN d.terms t WHERE t.patient.id = :patientId AND t.doctor.id = :doctorId AND t.endTime < :todayTime")
    Doctor checkIfPatientHadAppointmentWithDoctor(@Param("doctorId") UUID doctorId, @Param("patientId") UUID patientId, @Param("todayTime") Date todayTime);

    @Transactional(readOnly = true)
    @Query("SELECT d from doctor d JOIN d.terms t WHERE t.id = :termId")
    Doctor findDoctorByTermId(@Param("termId")UUID termId);

    @Query("SELECT p from work_schedule w inner join pharmacy p on w.pharmacy.id=p.id where w.doctor.id=:doctorId")
    List<Pharmacy> getDoctorPharmacyList(UUID doctorId);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE doctor SET id=:newId " +
            "WHERE id = :doctorId")
    int setId(UUID doctorId,UUID newId);

    @Query("SELECT p from work_schedule w inner join pharmacy p on w.pharmacy.id=p.id where w.doctor.id=:doctorId")
    Pharmacy getPharmPharmacy(UUID doctorId);

}
