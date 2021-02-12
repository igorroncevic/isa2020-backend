package team18.pharmacyapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import team18.pharmacyapp.model.Complaint;

import java.util.List;
import java.util.UUID;

public interface ComplaintRepository extends JpaRepository<Complaint, UUID> {

    @Query(value = "SELECT c FROM  Complaint  c WHERE  c.id=:complaintId")
    Complaint findComplaintById(UUID complaintId);

    @Transactional
    @Modifying
    @Query(nativeQuery = true,value = "insert into complaint (id,patient_id,complaint_text,doctor_id) " +
            "values (:id,:patientId,:text,:doctorId)")
    int saveComplaintDoctor(UUID id,UUID patientId,String text,UUID doctorId);

    @Transactional
    @Modifying
    @Query(nativeQuery = true,value = "insert into complaint (id,patient_id,complaint_text,pharmacy_id) " +
            "values (:id,:patientId,:text,:pharmacyId)")
    int saveComplaintPharmacy(UUID id,UUID patientId,String text,UUID pharmacyId);

    @Transactional
    @Modifying
    @Query(nativeQuery = true,value = "insert into complaint (id, admin_response) " +
            "values (:id, :text)")
    int saveComplaintResponse(UUID id, String text);

    @Transactional
    @Query("select c from Complaint c join fetch c.doctor join fetch c.pharmacy ")
    List<Complaint> getAllPatient();
}
