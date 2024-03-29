package team18.pharmacyapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team18.pharmacyapp.model.Report;

import java.util.UUID;

public interface ReportRepository extends JpaRepository <Report, UUID> {

}
