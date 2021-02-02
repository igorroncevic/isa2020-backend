package team18.pharmacyapp.service.interfaces;

import team18.pharmacyapp.model.Report;
import team18.pharmacyapp.model.dtos.ReportCreateDTO;

public interface ReportService {
    Report save(Report report);
    Report createNew(ReportCreateDTO report);
}
