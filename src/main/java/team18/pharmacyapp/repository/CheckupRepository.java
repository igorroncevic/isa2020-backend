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
    @Query(nativeQuery = true, value = "UPDATE term SET patient_id = :patientId WHERE id = :checkupId AND patient_id IS NULL")
    int patientScheduleCheckup(@Param("patientId") UUID patientId, @Param("checkupId") UUID checkupId);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE term SET patient_id = NULL WHERE id = :checkupId")
    int patientCancelCheckup(@Param("checkupId") UUID checkupId);

    @Transactional(readOnly = true)
    @Query(value = "SELECT t FROM term t JOIN FETCH t.doctor d WHERE t.type = :termType")
    List<Term> findAll(@Param("termType") TermType termType);

    @Transactional(readOnly = true)
    @Query(value = "SELECT t FROM term t JOIN FETCH t.patient p")
    List<Term> findAllWithPatients();

    @Transactional(readOnly = true)
    @Query(value = "SELECT t FROM term t JOIN FETCH t.doctor JOIN FETCH t.patient WHERE t.type = :termType AND t.patient.id = :patientId")
    List<Term> findAllPatientsCheckups(@Param("patientId") UUID patientId, @Param("termType") TermType termType);
}
