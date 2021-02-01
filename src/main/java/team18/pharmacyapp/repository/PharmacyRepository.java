package team18.pharmacyapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import team18.pharmacyapp.model.Pharmacy;
import team18.pharmacyapp.model.dtos.PharmacyFilteringDTO;

import java.util.List;
import java.util.UUID;

public interface PharmacyRepository extends JpaRepository<Pharmacy, UUID> {
    @Transactional(readOnly = true)
    @Query("SELECT new team18.pharmacyapp.model.dtos.PharmacyFilteringDTO(p.id, p.name, p.address, AVG(m.mark)) " +
            "FROM pharmacy p JOIN p.address a JOIN p.marks m GROUP BY p.id, p.name, p.address")
    List<PharmacyFilteringDTO> findAllForFiltering();

    @Transactional(readOnly = true)
    @Query("SELECT p FROM pharmacy p " +
            "JOIN p.workSchedules ws " +
            "JOIN ws.doctor d " +
            "JOIN d.terms t " +
            "WHERE (ws.pharmacy.id = :pharmacyId AND ws.doctor.id = :doctorId) AND (t.patient.id = :patientId AND t.doctor.id = :doctorId)")
    Pharmacy checkIfPatientHadAppointmentInPharmacy(@Param("pharmacyId") UUID pharmacyId, @Param("patientId")UUID patientId, @Param("doctorId") UUID doctorId);
}
