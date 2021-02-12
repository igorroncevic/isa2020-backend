package team18.pharmacyapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import team18.pharmacyapp.model.Complaint;
import team18.pharmacyapp.model.users.Doctor;

import java.util.UUID;

public interface ComplaintRepository extends JpaRepository<Complaint, UUID> {

}
