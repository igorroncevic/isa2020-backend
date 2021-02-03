package team18.pharmacyapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import team18.pharmacyapp.model.Vacation;
import team18.pharmacyapp.model.enums.VacationStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VacationRepository extends JpaRepository<Vacation, UUID> {

    @Query("SELECT v FROM vacation v JOIN FETCH v.doctor d WHERE v.status = :vacationStatus")
    List<Vacation> getAll(@Param("vacationStatus") VacationStatus vacationStatus);

    @Query("SELECT v FROM vacation v JOIN FETCH v.doctor d WHERE v.id = :vacationId")
    Optional<Vacation> getById(@Param("vacationId") UUID vacationId);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE vacation SET status = 'approved' WHERE id = :vacationId")
    int approve(@Param("vacationId") UUID vacationId);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE vacation SET status = 'refused', rejection_reason = :reason WHERE id = :vacationId")
    int refuse(@Param("vacationId") UUID vacationId, @Param("reason") String reason);

}
