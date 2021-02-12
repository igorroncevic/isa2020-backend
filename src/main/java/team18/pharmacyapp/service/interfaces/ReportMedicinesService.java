package team18.pharmacyapp.service.interfaces;

import team18.pharmacyapp.model.medicine.ReportMedicines;

import java.util.UUID;

public interface ReportMedicinesService {
    ReportMedicines save(ReportMedicines reportMedicines, UUID pharmacyId);
}
