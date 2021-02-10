package team18.pharmacyapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import team18.pharmacyapp.model.Term;
import team18.pharmacyapp.model.medicine.MedicineRequests;

import java.util.Date;
import java.util.UUID;

public interface MedicineRequestRepository extends JpaRepository<MedicineRequests, UUID> {
    @Query(nativeQuery = true,value="select Cast(id as varchar ) id from medicine_requests where  pharmacy_medicine_medicine_id=:medicineId")
    String getByMedicineAndPharmacy(UUID medicineId);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE medicine_requests SET times_requested = times_requested + 1 " +
            "WHERE id = :mrId")
    int addRequest(UUID mrId);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "INSERT INTO medicine_requests(id, times_requested, pharmacy_medicine_medicine_id, pharmacy_medicine_pharmacy_id) " +
            "VALUES (:id,1,:medicineId,:pharmacyId)")
    int createNew(UUID id,UUID medicineId,UUID pharmacyId);
}
