package team18.pharmacyapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team18.pharmacyapp.model.dtos.ReportMedicineDTO;
import team18.pharmacyapp.model.medicine.Medicine;
import team18.pharmacyapp.repository.PharmacyMedicinesRepository;
import team18.pharmacyapp.service.interfaces.MedicineService;
import team18.pharmacyapp.service.interfaces.PharmacyMedicinesService;

import java.util.List;
import java.util.UUID;

@Service
public class PharmacyMedicinesServiceImpl implements PharmacyMedicinesService {
    private final PharmacyMedicinesRepository repository;
    private final MedicineService medicineService;

    @Autowired
    public PharmacyMedicinesServiceImpl(PharmacyMedicinesRepository repository, MedicineService medicineService) {
        this.repository = repository;
        this.medicineService = medicineService;
    }

    @Override
    public int medicineQuantity(UUID pharmacyId, UUID medicineId) {
        return repository.findQyantity(pharmacyId,medicineId);
    }

    @Override
    public String checkAvailability(ReportMedicineDTO dto) {
        int quantity=medicineQuantity(dto.getPharmacyId(),dto.getMedicineId());
        if(quantity<dto.getMedicineQuantity()){
            String replacment=medicineService.getReplacmentMedicine(dto.getMedicineId());
            if(replacment!=null){
                return replacment;
            }
            return "unavailable";
        }
        return "available";
    }

    @Override
    public List<Medicine> getMedicnesByPharmacy(UUID pharmacy) {
        return repository.getMedicineByPharmacy(pharmacy);
    }

}
