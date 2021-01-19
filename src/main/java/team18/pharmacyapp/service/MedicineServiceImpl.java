package team18.pharmacyapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team18.pharmacyapp.model.Pricings;
import team18.pharmacyapp.model.dtos.PharmacyMedicinesDTO;
import team18.pharmacyapp.model.dtos.ReservedMedicineDTO;
import team18.pharmacyapp.model.dtos.ReserveMedicineRequestDTO;
import team18.pharmacyapp.model.medicine.Medicine;
import team18.pharmacyapp.model.medicine.PharmacyMedicines;
import team18.pharmacyapp.model.medicine.ReserveMedicineException;
import team18.pharmacyapp.model.medicine.ReservedMedicines;
import team18.pharmacyapp.model.users.Patient;
import team18.pharmacyapp.repository.MedicineRepository;
import team18.pharmacyapp.service.interfaces.MedicineService;

import javax.persistence.LockModeType;
import java.sql.SQLException;
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
    public List<ReservedMedicineDTO> findAllPatientsReservedMedicines(UUID id) {
        Patient patient = new Patient();
        patient.setId(id);
        List<ReservedMedicineDTO> reservedMedicines = medicineRepository.findAllPatientsReservedMedicines(patient);
        List<ReservedMedicineDTO> resultSet = new ArrayList<>();

        for(ReservedMedicineDTO rmDTO : reservedMedicines) {
            List<Pricings> pricings = rmDTO.getPricings();
            double finalPrice = -1.0;

            for(Pricings pricing : pricings){
                if(pricing.getStartDate().before(rmDTO.getPickupDate()) && pricing.getEndDate().after(rmDTO.getPickupDate())){
                    finalPrice = pricing.getPrice();
                    break;
                }
            }

            if(finalPrice == -1.0) continue;

            ReservedMedicineDTO finalRmDTO = new ReservedMedicineDTO();
            finalRmDTO.setMedicine(rmDTO.getMedicine());
            finalRmDTO.setPharmacy(rmDTO.getPharmacy());
            finalRmDTO.setPrice(finalPrice);
            finalRmDTO.setPickupDate(rmDTO.getPickupDate());

            resultSet.add(finalRmDTO);
        }

        return resultSet;
    }

    @Transactional(rollbackFor = {RuntimeException.class, ReserveMedicineException.class})
    @Override
    public boolean reserveMedicine(ReserveMedicineRequestDTO rmrDTO) throws ReserveMedicineException, RuntimeException {
        int reserved = medicineRepository.reserveMedicine(UUID.randomUUID(), rmrDTO.getPatient_id(), rmrDTO.getPharmacy_id(), rmrDTO.getMedicine_id(), rmrDTO.getPickupDate());
        int updateQuantity = medicineRepository.updateMedicineQuantity(rmrDTO.getMedicine_id(), rmrDTO.getPharmacy_id());

        if (reserved != 1) throw new ReserveMedicineException("Medicine wasn't reserved!"); // rollback-ovace transakciju
        if (updateQuantity != 1) throw new ReserveMedicineException("Medicine quantity wasn't updated!");

        return true;
    }

    @Override
    public boolean cancelMedicine(ReserveMedicineRequestDTO reserveMedicineRequestDTO) {
        return false;
    }
}