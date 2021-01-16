package team18.pharmacyapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import team18.pharmacyapp.model.Term;

import java.util.List;
import java.util.UUID;

public interface TermRepository extends JpaRepository<Term, UUID> {
    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE term SET patient_id = :patientId WHERE id = :termId AND patient_id IS NULL")
    int patientScheduleCheckup(@Param("patientId") UUID patientId, @Param("termId") UUID termId);

    @Transactional(readOnly = true)
    @Query(value = "SELECT t FROM term t JOIN FETCH t.doctor d")
    List<Term> findAllCustom();
}
