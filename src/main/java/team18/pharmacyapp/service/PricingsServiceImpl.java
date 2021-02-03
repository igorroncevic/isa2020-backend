package team18.pharmacyapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team18.pharmacyapp.model.Pricings;
import team18.pharmacyapp.model.dtos.PricingsDTO;
import team18.pharmacyapp.repository.PricingsRepository;
import team18.pharmacyapp.service.interfaces.PricingsService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class PricingsServiceImpl implements PricingsService {

    private final PricingsRepository pricingsRepository;

    @Autowired
    public PricingsServiceImpl(PricingsRepository pricingsRepository) {
        this.pricingsRepository = pricingsRepository;
    }

    @Override
    public List<PricingsDTO> getAllCurrentPricingsForPhramcy(UUID id) {
        List<Pricings> pricings = pricingsRepository.getAllCurrentPricingsForPharmacy(id, new Date());
        List<PricingsDTO> pricingsDTOs = new ArrayList<>();
        for(Pricings pricing : pricings) {
            PricingsDTO pricingsDTO = new PricingsDTO();
            pricingsDTO.setId(pricing.getId());
            pricingsDTO.setMedicine(pricing.getPharmacyMedicine().getMedicine().getName());
            pricingsDTO.setMedicineId(pricing.getPharmacyMedicine().getMedicine().getId());
            pricingsDTO.setStartDate(pricing.getStartDate());
            pricingsDTO.setEndDate(pricing.getEndDate());
            pricingsDTO.setPrice(pricing.getPrice());
            pricingsDTOs.add(pricingsDTO);
        }
        return pricingsDTOs;
    }

    @Override
    public List<PricingsDTO> getAllPricingsForMedicine(UUID phId, UUID mId) {
        List<Pricings> pricings = pricingsRepository.getAllPricingsForMedicine(phId, mId);
        List<PricingsDTO> pricingsDTOs = new ArrayList<>();
        for(Pricings pricing : pricings) {
            PricingsDTO pricingsDTO = new PricingsDTO();
            pricingsDTO.setId(pricing.getId());
            pricingsDTO.setMedicine(pricing.getPharmacyMedicine().getMedicine().getName());
            pricingsDTO.setMedicineId(pricing.getPharmacyMedicine().getMedicine().getId());
            pricingsDTO.setStartDate(pricing.getStartDate());
            pricingsDTO.setEndDate(pricing.getEndDate());
            pricingsDTO.setPrice(pricing.getPrice());
            pricingsDTOs.add(pricingsDTO);
        }
        return pricingsDTOs;
    }
}
