package team18.pharmacyapp.service;

import org.springframework.stereotype.Service;
import team18.pharmacyapp.model.medicine.ReportMedicines;
import team18.pharmacyapp.repository.ReportMedicinesRepository;
import team18.pharmacyapp.service.interfaces.PharmacyMedicinesService;
import team18.pharmacyapp.service.interfaces.ReportMedicinesService;

@Service
public class ReportMedicinesServiceImpl implements ReportMedicinesService {
    private final ReportMedicinesRepository repository;
    private final PharmacyMedicinesService medicinesService;

    public ReportMedicinesServiceImpl(ReportMedicinesRepository repository, PharmacyMedicinesService medicinesService) {
        this.repository = repository;
        this.medicinesService = medicinesService;
    }

    @Override
    public ReportMedicines save(ReportMedicines reportMedicines) {
        if (!reportMedicines.getStartDate().before(reportMedicines.getEndDate())) {
            return null;
        }
        return repository.save(reportMedicines);
    }
}
