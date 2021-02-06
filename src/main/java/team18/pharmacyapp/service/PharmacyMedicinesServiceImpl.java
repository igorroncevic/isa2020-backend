package team18.pharmacyapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team18.pharmacyapp.model.dtos.MedicineQuantityDTO;
import team18.pharmacyapp.model.dtos.ReportMedicineDTO;
import team18.pharmacyapp.model.exceptions.ActionNotAllowedException;
import team18.pharmacyapp.model.medicine.PharmacyMedicines;
import team18.pharmacyapp.repository.PharmacyMedicinesRepository;
import team18.pharmacyapp.service.interfaces.PharmacyMedicinesService;

import java.util.ArrayList;
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
    public List<MedicineQuantityDTO> getMedicnesByPharmacy(UUID pharmacy) {
        List<PharmacyMedicines> pharmacyMedicines = repository.getMedicineByPharmacy(pharmacy);
        List<MedicineQuantityDTO> medicineQuantityDTOs = new ArrayList<>();
        for(PharmacyMedicines pharmacyMedicine : pharmacyMedicines) {
            MedicineQuantityDTO medicineQuantityDTO = new MedicineQuantityDTO();
            medicineQuantityDTO.setId(pharmacyMedicine.getMedicine().getId());
            medicineQuantityDTO.setName(pharmacyMedicine.getMedicine().getName());
            medicineQuantityDTO.setLoyaltyPoints(pharmacyMedicine.getMedicine().getLoyaltyPoints());
            medicineQuantityDTO.setQuantity(pharmacyMedicine.getQuantity());
            medicineQuantityDTOs.add(medicineQuantityDTO);
        }
        return medicineQuantityDTOs;
    }

    @Override
    public void insert(UUID pharmacyId, UUID medicineId) {
        repository.insert(pharmacyId, medicineId);
    }

}
