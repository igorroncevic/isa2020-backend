package team18.pharmacyapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import team18.pharmacyapp.model.Term;

import java.util.Date;
import java.util.List;
import java.util.UUID;

// For general use, not with specific types like Checkup or Counseling
public interface TermRepository extends JpaRepository<Term, UUID> {
    @Transactional(readOnly = true)
    @Query("SELECT t FROM term t " +
            "JOIN t.patient pat " +
            "JOIN t.doctor d " +
            "JOIN d.workSchedules ws " +
            "JOIN ws.pharmacy pha " +
            "WHERE pha.id = :pharmacyId AND pat.id = :patientId")
    List<Term> getPatientsTermsFromPharmacy(@Param("pharmacyId")UUID pharmacyId, @Param("patientId") UUID patientId);


    @Query("SELECT t FROM term t join fetch t.patient p WHERE t.doctor.id = :doctorId ")
    List<Term> findAllTermsForDoctor(@Param("doctorId") UUID doctorId);

    @Query("SELECT t FROM term t WHERE t.doctor.id = :doctorId and t.patient is null ")
    List<Term> findAllFreeTermsForDoctor(@Param("doctorId") UUID doctorId);


    @Query("SELECT t FROM term t inner join work_schedule w on t.doctor.id=w.doctor.id join fetch t.patient p WHERE t.doctor.id = :doctorId and w.pharmacy.id=:pharmacyId " +
            "and t.startTime>=w.fromHour and t.endTime<=w.toHour")
    List<Term> findAllTermsForDoctorInPharmacy(@Param("doctorId") UUID doctorId,@Param("pharmacyId")UUID pharmacyId);

    @Query("SELECT t FROM term t inner join work_schedule w on t.doctor.id=w.doctor.id WHERE t.doctor.id = :doctorId and t.patient is null and w.pharmacy.id=:pharmacyId " +
            "and t.startTime>=w.fromHour and t.endTime<=w.toHour")
    List<Term> findAllFreeTermsForDoctorInPharmacy(@Param("doctorId") UUID doctorId,@Param("pharmacyId")UUID pharmacyId);


    @Query("SELECT t FROM term t WHERE t.patient.id = :patientId ")
    List<Term> findAllTermsForPatient(@Param("patientId") UUID patientId);

    @Query("select t from term t where t.patient.id=:patientId and t.doctor.id=:doctorId and :time>=t.startTime and :time<t.endTime")
    Term findTermByDoctorAndPatientAndTime(UUID patientId, UUID doctorId, Date time);



}
