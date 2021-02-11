package team18.pharmacyapp.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import team18.pharmacyapp.model.Term;
import team18.pharmacyapp.model.dtos.PharmacyMarkPriceDTO;
import team18.pharmacyapp.model.enums.TermType;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Repository
public interface CounselingRepository extends JpaRepository<Term, UUID> {
    @Transactional(readOnly = true)
    @Query("SELECT new team18.pharmacyapp.model.dtos.PharmacyMarkPriceDTO(p.id, p.name, a.street, a.city, " +
            "a.country, min(t.price), max(t.price), avg(m.mark)) " +
            "FROM pharmacy p JOIN p.marks m " +
            "                JOIN p.address a " +
            "                JOIN p.workSchedules ws " +
            "                JOIN ws.doctor d " +
            "                JOIN d.terms t " +
            "WHERE t.type = 'counseling' AND d.role = 'pharmacist' " +
            "GROUP BY p.id, p.name, a.street, a.city, a.country")
    List<PharmacyMarkPriceDTO> getPharmaciesWithAvailableCounselings(@Param("fromTime") Date fromTime, @Param("toTime") Date toTime);

    @Query("SELECT t FROM term t WHERE t.doctor.id = :doctorId AND t.type = 'counseling'")
    List<Term> findAllCounselingsForDoctor(@Param("doctorId") UUID doctorId);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "INSERT INTO term(id, patient_id, doctor_id, start_time, end_time, price, type, report_id) " +
            "VALUES (:id, :patientId, :doctorId, :startTime, :endTime, 10, 'counseling', null)")
        // Cijeni i poeni zakucani, u specifikaciji ne pise kako se definisu
    int patientScheduleCounseling(@Param("id") UUID id, @Param("patientId") UUID patientId, @Param("doctorId") UUID doctorId,
                                  @Param("startTime") Date startTime, @Param("endTime") Date endTime);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE term SET patient_id = NULL WHERE id = :counselingId")
    int patientCancelCounseling(@Param("counselingId") UUID counselingId);

    @Transactional(readOnly = true)
    @Query("SELECT t FROM term t WHERE t.startTime >= :todaysDate AND t.patient.id = :patientId AND t.doctor.id = :doctorId")
    Term checkIfPatientHasCounselingWithDoctor(@Param("patientId") UUID patientId, @Param("doctorId")UUID doctorId, @Param("todaysDate") Date todaysDate);

    @Transactional(readOnly = true)
    @Query("SELECT t FROM term t JOIN FETCH t.patient WHERE t.id = :counselingId")
    Term findByIdCustom(@Param("counselingId") UUID id);

    @Transactional(readOnly = true)
    @Query(value = "SELECT t FROM term t JOIN FETCH t.doctor JOIN t.patient WHERE t.type = :termType AND t.patient.id = :patientId")
    List<Term> findAllPatientsCounselings(@Param("patientId") UUID id, @Param("termType") TermType counseling);
}
