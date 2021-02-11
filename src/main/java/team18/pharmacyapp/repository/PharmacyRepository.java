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

    Pharmacy findByName(String name);

    @Transactional(readOnly = true)
    @Query("SELECT p FROM pharmacy p JOIN FETCH p.address WHERE " +
            "p.id IN (SELECT distinct p.id FROM pharmacy p JOIN p.workSchedules ws JOIN ws.doctor d JOIN d.terms t WHERE t.patient.id = :patientId) OR " +
            "p.id IN (SELECT distinct p.id FROM pharmacy p JOIN p.reservedMedicines rm WHERE rm.patient.id = :patientId) OR " +
            "p.id IN (SELECT distinct p.id FROM pharmacy p JOIN p.pharmacyMedicines pm JOIN pm.ePrescriptionMedicines epm JOIN epm.ePrescription ep WHERE ep.patient.id = :patientId)")
    List<Pharmacy> getPatientsPharmacies(@Param("patientId") UUID patientId);

    @Transactional(readOnly = true)
    @Query("SELECT new team18.pharmacyapp.model.dtos.PharmacyFilteringDTO(p.id, p.name, p.address, AVG(m.mark)) " +
            "FROM pharmacy p JOIN p.address a JOIN p.marks m JOIN p.pharmacyMedicines pm " +
            "WHERE pm.medicine.id = :medicineId " +
            "GROUP BY p.id, p.name, p.address, pm.quantity " +
            "HAVING pm.quantity > 0")
    List<PharmacyFilteringDTO> getAllPharmaciesForMedicine(@Param("medicineId") UUID medicineId);

    @Transactional(readOnly = true)
    @Query("SELECT p FROM pharmacy p JOIN FETCH p.address WHERE p.id = :id")
    Pharmacy findByIdCustom(@Param("id") UUID id);
}
