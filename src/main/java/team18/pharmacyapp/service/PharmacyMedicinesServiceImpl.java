package team18.pharmacyapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team18.pharmacyapp.model.dtos.ReportMedicineDTO;
import team18.pharmacyapp.model.medicine.Medicine;
import team18.pharmacyapp.model.medicine.MedicineSpecification;
import team18.pharmacyapp.repository.PharmacyMedicinesRepository;
import team18.pharmacyapp.service.interfaces.PharmacyMedicinesService;

import java.util.List;
import java.util.UUID;

@Service
public class PharmacyMedicinesServiceImpl implements PharmacyMedicinesService {
    private final PharmacyMedicinesRepository repository;

    @Autowired
    public PharmacyMedicinesServiceImpl(PharmacyMedicinesRepository repository) {
        this.repository = repository;
    }

    @Override
    public int medicineQuantity(UUID pharmacyId, UUID medicineId) {
        return repository.findQyantity(pharmacyId,medicineId);
    }

    @Override
    public boolean checkAvailability(ReportMedicineDTO dto) {
        int quantity=medicineQuantity(dto.getPharmacyId(),dto.getMedicineId());
        if(quantity<dto.getMedicineQuantity()){
            return false;
        }
        return true;
    }

    @Override
    public List<Medicine> getMedicnesByPharmacy(UUID pharmacy) {
        return repository.getMedicineByPharmacy(pharmacy);
    }

}
