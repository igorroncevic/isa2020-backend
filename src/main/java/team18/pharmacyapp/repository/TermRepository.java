package team18.pharmacyapp.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import team18.pharmacyapp.model.Term;
import team18.pharmacyapp.model.enums.TermType;

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

    //----- Not finished yet
    @Query("SELECT t FROM term t join fetch t.patient p WHERE t.doctor.id = :doctorId")
    List<Term> findAllTermsForDoctorInPharmacy(@Param("doctorId") UUID doctorId);

    @Query("SELECT t FROM term t WHERE t.doctor.id = :doctorId and t.patient is null ")
    List<Term> findAllFreeTermsForDoctorInPharmacy(@Param("doctorId") UUID doctorId);
    //------------------

    @Query("SELECT t FROM term t WHERE t.patient.id = :patientId ")
    List<Term> findAllTermsForPatient(@Param("patientId") UUID patientId);

    @Transactional(readOnly = true)
    Page<Term> findAllByPatient_IdAndTypeAndStartTimeBefore(@Param("patientId") UUID id, @Param("termType") TermType checkup, @Param("today") Date today, Pageable pageable);

    @Transactional(readOnly = true)
    Page<Term> findAllByPatient_IdAndTypeAndStartTimeAfter(@Param("patientId") UUID id, @Param("termType") TermType checkup, @Param("today") Date today, Pageable pageable);
}
