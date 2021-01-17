package team18.pharmacyapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import team18.pharmacyapp.model.Term;
import team18.pharmacyapp.model.enums.TermType;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface CheckupRepository extends JpaRepository<Term, UUID> {
    @Transactional(readOnly = true)
    @Query(value = "SELECT t FROM term t JOIN FETCH t.doctor d WHERE t.patient IS NULL AND t.startTime > :todaysDate AND t.type = :termType")
    List<Term> findAllAvailableCheckups(@Param("todaysDate") Date todaysDate, @Param("termType") TermType termType);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE term SET patient_id = :patientId WHERE id = :termId AND patient_id IS NULL")
    int patientScheduleCheckup(@Param("patientId") UUID patientId, @Param("termId") UUID termId);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE term SET patient_id = NULL WHERE id = :termId")
    int patientCancelCheckup(@Param("termId") UUID termId);

    @Transactional(readOnly = true)
    @Query(value = "SELECT t FROM term t JOIN FETCH t.doctor d WHERE t.type = :termType")
    List<Term> findAll(@Param("termType") TermType termType);
}
