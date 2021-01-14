package team18.pharmacyapp.repository.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import team18.pharmacyapp.model.Term;
import team18.pharmacyapp.model.dtos.TermDTO;

import java.util.UUID;

public interface TermRepository extends JpaRepository<Term, UUID> {
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE term SET patient_id = :patient_id WHERE id = :id")
    int patientScheduleCheckup(@Param("patient_id")UUID patient_id, @Param("id") UUID term_id);
}
