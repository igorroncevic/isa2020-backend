package team18.pharmacyapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import team18.pharmacyapp.model.Vacation;

import java.util.List;
import java.util.UUID;

public interface VacationRepository extends JpaRepository<Vacation, UUID> {

    @Query("SELECT v FROM vacation v JOIN FETCH v.doctor d WHERE v.status = 'pending'")
    public List<Vacation> getAllPending();

}
