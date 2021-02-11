package team18.pharmacyapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import team18.pharmacyapp.model.Mark;

import java.util.UUID;

public interface MarkRepository extends JpaRepository<Mark, UUID> {
    @Transactional(readOnly = true)
    @Query("SELECT COALESCE(AVG(m.mark), 0) FROM mark m WHERE m.doctor.id = :doctorId")
    Float getAverageMarkForDoctor(@Param("doctorId") UUID doctorId);

    @Transactional(readOnly = true)
    @Query("SELECT COALESCE(AVG(m.mark), 0) FROM mark m WHERE m.medicine.id = :medicineId")
    Float getAverageMarkForMedicine(@Param("medicineId") UUID medicineId);

    @Transactional(readOnly = true)
    @Query("SELECT COALESCE(AVG(m.mark), 0.0) FROM mark m WHERE m.pharmacy.id = :id")
    Float getAverageMarkForPharmacy(@Param("id") UUID id);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value =
            "INSERT into mark(id, mark, doctor_id, medicine_id, patient_id, pharmacy_id) " +
            "values (:id, :markValue, :doctorId, null, :patientId, null)")
    int giveMarkToDoctor(@Param("id")UUID id, @Param("markValue")int markValue, @Param("doctorId")UUID doctorId, @Param("patientId")UUID patientId);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value =
            "INSERT into mark(id, mark, doctor_id, medicine_id, patient_id, pharmacy_id) " +
                    "values (:id, :markValue, null, :medicineId, :patientId, null)")
    int giveMarkToMedicine(@Param("id")UUID id, @Param("markValue")int markValue, @Param("medicineId")UUID medicineId, @Param("patientId")UUID patientId);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value =
            "INSERT into mark(id, mark, doctor_id, medicine_id, patient_id, pharmacy_id) " +
                    "values (:id, :markValue, null, null, :patientId, :pharmacyId)")
    int giveMarkToPharmacy(@Param("id")UUID id, @Param("markValue")int markValue, @Param("pharmacyId")UUID pharmacyId, @Param("patientId")UUID patientId);

    @Transactional(readOnly = true)
    @Query("SELECT m FROM mark m WHERE m.doctor.id = :doctorId AND m.patient.id = :patientId")
    Mark checkIfPatientHasGivenMarkToDoctor(@Param("doctorId")UUID doctorId, @Param("patientId")UUID patientId);

    @Transactional(readOnly = true)
    @Query("SELECT m FROM mark m WHERE m.pharmacy.id = :pharmacyId AND m.patient.id = :patientId")
    Mark checkIfPatientHasGivenMarkToPharmacy(@Param("pharmacyId")UUID pharmacyId, @Param("patientId")UUID patientId);

    @Transactional(readOnly = true)
    @Query("SELECT m FROM mark m WHERE m.medicine.id = :medicineId AND m.patient.id = :patientId")
    Mark checkIfPatientHasGivenMarkToMedicine(@Param("medicineId")UUID medicineId, @Param("patientId")UUID patientId);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE mark SET mark = :markValue WHERE doctor_id = :doctorId AND patient_id = :patientId")
    int updateDoctorsMark(@Param("markValue") int markValue, @Param("doctorId") UUID doctorId, @Param("patientId") UUID patientId);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE mark SET mark = :markValue WHERE pharmacy_id = :pharmacyId AND patient_id = :patientId")
    int updatePharmacysMark(@Param("markValue") int markValue, @Param("pharmacyId") UUID pharmacyId, @Param("patientId") UUID patientId);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE mark SET mark = :markValue WHERE medicine_id = :medicineId AND patient_id = :patientId")
    int updateMedicinesMark(@Param("markValue") int markValue, @Param("medicineId") UUID medicineId, @Param("patientId") UUID patientId);
}
