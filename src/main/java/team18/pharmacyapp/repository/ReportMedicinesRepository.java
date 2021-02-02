package team18.pharmacyapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team18.pharmacyapp.model.keys.ReportMedicineId;
import team18.pharmacyapp.model.medicine.ReportMedicines;

public interface ReportMedicinesRepository extends JpaRepository<ReportMedicines, ReportMedicineId> {

}
