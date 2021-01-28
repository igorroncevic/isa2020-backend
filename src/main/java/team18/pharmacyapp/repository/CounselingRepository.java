package team18.pharmacyapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import team18.pharmacyapp.model.Term;
import team18.pharmacyapp.model.dtos.PharmacyMarkPriceDTO;

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
}
