package team18.pharmacyapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import team18.pharmacyapp.model.Term;

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
}
