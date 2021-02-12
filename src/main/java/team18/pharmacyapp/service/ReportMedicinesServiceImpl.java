package team18.pharmacyapp.service;

import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team18.pharmacyapp.model.medicine.ReportMedicines;
import team18.pharmacyapp.repository.ReportMedicinesRepository;
import team18.pharmacyapp.service.interfaces.PharmacyMedicinesService;
import team18.pharmacyapp.service.interfaces.ReportMedicinesService;

import javax.persistence.LockModeType;
import java.util.UUID;

@Service
public class ReportMedicinesServiceImpl implements ReportMedicinesService {
    private final ReportMedicinesRepository repository;
    private final PharmacyMedicinesService medicinesService;

    public ReportMedicinesServiceImpl(ReportMedicinesRepository repository, PharmacyMedicinesService medicinesService) {
        this.repository = repository;
        this.medicinesService = medicinesService;
    }

    @Override
    @Transactional
    @Lock(LockModeType.WRITE)
    public ReportMedicines save(ReportMedicines reportMedicines, UUID pharmacyId) {
        if (!reportMedicines.getStartDate().before(reportMedicines.getEndDate())) {
            return null;
        }
        int valide=medicinesService.updateQuantity(reportMedicines.getMedicine().getId(),pharmacyId,reportMedicines.getMedicineQuantity());
        if(valide!=0){
            return repository.save(reportMedicines);
        }
        return null;
    }
}
