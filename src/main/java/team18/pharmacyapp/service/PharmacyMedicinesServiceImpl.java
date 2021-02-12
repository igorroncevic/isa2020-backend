package team18.pharmacyapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team18.pharmacyapp.model.dtos.MedicineQuantityDTO;
import team18.pharmacyapp.model.dtos.ReportMedicineDTO;
import team18.pharmacyapp.model.exceptions.ActionNotAllowedException;
import team18.pharmacyapp.model.keys.PharmacyMedicinesId;
import team18.pharmacyapp.model.medicine.PharmacyMedicines;
import team18.pharmacyapp.repository.PharmacyMedicinesRepository;
import team18.pharmacyapp.service.interfaces.MedicineRequestsService;
import team18.pharmacyapp.service.interfaces.MedicineService;
import team18.pharmacyapp.service.interfaces.PharmacyMedicinesService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class PharmacyMedicinesServiceImpl implements PharmacyMedicinesService {
    private final PharmacyMedicinesRepository repository;
    private final MedicineService medicineService;
    private final MedicineRequestsService medicineRequestsService;

    @Autowired
    public PharmacyMedicinesServiceImpl(PharmacyMedicinesRepository repository, MedicineService medicineService, MedicineRequestsService medicineRequestsService) {
        this.repository = repository;
        this.medicineService = medicineService;
        this.medicineRequestsService = medicineRequestsService;
    }

    @Override
    public int medicineQuantity(UUID pharmacyId, UUID medicineId) {
        return repository.findQyantity(pharmacyId, medicineId);
    }

    @Override
    public String checkAvailability(ReportMedicineDTO dto) {
        int quantity = medicineQuantity(dto.getPharmacyId(), dto.getMedicineId());
        if (quantity < dto.getMedicineQuantity()) {
            medicineRequestsService.checkRequest(dto.getMedicineId(), dto.getPharmacyId());
            String replacment = medicineService.getReplacmentMedicine(dto.getMedicineId());
            if (replacment != null) {
                return replacment;
            }
            return "unavailable";
        }
        return "available";
    }

    @Override
    public int updateQuantity(UUID medicineId, UUID pharmacyId, int quantity) {
        return repository.updateQuantity(medicineId,pharmacyId,quantity);
    }

    @Override
    public List<MedicineQuantityDTO> getMedicnesByPharmacy(UUID pharmacy) {
        List<PharmacyMedicines> pharmacyMedicines = repository.getMedicineByPharmacy(pharmacy);
        List<MedicineQuantityDTO> medicineQuantityDTOs = new ArrayList<>();
        for (PharmacyMedicines pharmacyMedicine : pharmacyMedicines) {
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

    @Override
    public void deletePharmacyMedicine(PharmacyMedicinesId pharmacyMedicinesId) throws ActionNotAllowedException {
        int numberOfUnhandledRegistrations = repository.getNumberOfUnhandledReservations(pharmacyMedicinesId.getPharmacy(), pharmacyMedicinesId.getMedicine());
        if (numberOfUnhandledRegistrations != 0)
            throw new ActionNotAllowedException("You can't delete this pharmacy medicine because it has unhandled reservations.");
        repository.deleteById(pharmacyMedicinesId);
    }
}
