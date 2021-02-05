package team18.pharmacyapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import team18.pharmacyapp.model.WorkSchedule;
import team18.pharmacyapp.model.keys.WorkScheduleId;
import java.util.UUID;

public interface WorkScheduleRepository extends JpaRepository<WorkSchedule, WorkScheduleId> {
    @Transactional(readOnly = true)
    @Query("SELECT w FROM work_schedule w WHERE w.doctor.id=:doctorId and w.pharmacy.id=:pharmacyId")
    WorkSchedule getDoctorSchedule(UUID doctorId, UUID pharmacyId);
}
