package team18.pharmacyapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team18.pharmacyapp.model.Pricings;
import team18.pharmacyapp.model.dtos.PharmacyMedicinesDTO;
import team18.pharmacyapp.model.dtos.ReserveMedicineDTO;
import team18.pharmacyapp.model.medicine.Medicine;
import team18.pharmacyapp.model.medicine.PharmacyMedicines;
import team18.pharmacyapp.repository.MedicineRepository;
import team18.pharmacyapp.service.interfaces.MedicineService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class MedicineServiceImpl implements MedicineService {
    private MedicineRepository medicineRepository;

    @Autowired
    public MedicineServiceImpl(MedicineRepository medicineRepository){
        this.medicineRepository = medicineRepository;
    }

    @Override
    public List<Medicine> findAll() {
        return medicineRepository.findAll();
    }

    @Override
    public Medicine save(Medicine medicine) {
        return medicineRepository.save(medicine);
    }

    @Override
    public Medicine findById(UUID id) {
        return medicineRepository.findById(id).orElseGet(null);
    }

    @Override
    public void deleteById(UUID id) {
        medicineRepository.deleteById(id);
    }

    @Override
    public List<PharmacyMedicinesDTO> findAllAvailableMedicines() {
        List<PharmacyMedicines> pharmacyMedicines = medicineRepository.findAllAvailableMedicines();
        List<PharmacyMedicinesDTO> resultSet = new ArrayList<>();

        Date todaysDate = new Date(System.currentTimeMillis() + 10 * 1000);   //mali offset
        for(PharmacyMedicines pm : pharmacyMedicines){
            List<Pricings> pricings = pm.getPricings();
            double finalPrice = -1.0;

            for(Pricings pricing : pricings){
                if(pricing.getStartDate().before(todaysDate) && pricing.getEndDate().after(todaysDate)){
                    finalPrice = pricing.getPrice();
                    break;
                }
            }

            if(finalPrice == -1) continue;

            PharmacyMedicinesDTO pmDTO = new PharmacyMedicinesDTO();
            pmDTO.setMedicine(pm.getMedicine());
            pmDTO.setPharmacy(pm.getPharmacy());
            pmDTO.setPrice(finalPrice);
            pmDTO.setQuantity(pm.getQuantity());

            resultSet.add(pmDTO);
        }

        return resultSet;
    }

    @Override
    public List<Medicine> findAllPatientsReservedMedicines(UUID id) {
        return null;
    }

    @Override
    public boolean reserveMedicine(ReserveMedicineDTO reserveMedicineDTO) {
        return false;
    }

    @Override
    public boolean cancelMedicine(ReserveMedicineDTO reserveMedicineDTO) {
        return false;
    }
}
