package team18.pharmacyapp.service;

import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team18.pharmacyapp.model.Pricings;
import team18.pharmacyapp.model.dtos.NewPricingDTO;
import team18.pharmacyapp.model.dtos.PricingsDTO;
import team18.pharmacyapp.model.exceptions.ActionNotAllowedException;
import team18.pharmacyapp.model.exceptions.BadTimeRangeException;
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

    @Override
    public void deletePricing(UUID id) throws NotFoundException, ActionNotAllowedException {
        Pricings pricings = pricingsRepository.findById(id).orElse(null);
        if(pricings == null) {
            throw new NotFoundException("Pricing not found");
        }

        Date today = new Date();
        if(!pricings.getStartDate().after(today)) {
            throw new ActionNotAllowedException("You can't delete pricing that have passed or is currently active.");
        }

        pricingsRepository.deleteById(id);
    }

    @Override
    public Pricings addNewPricing(NewPricingDTO newPricingDTO) throws BadTimeRangeException {

        int overlappingPricings = pricingsRepository.getNumberOfOverlappingPricings(newPricingDTO.getStartDate(), newPricingDTO.getEndDate(),
                newPricingDTO.getPharmacyId(), newPricingDTO.getMedicineId());
        if(overlappingPricings != 0) {
            throw new BadTimeRangeException("Pricing can't overlap with other pricings");
        }

        if(newPricingDTO.getStartDate().before(new Date())) {
            throw new BadTimeRangeException("You can't create pricing that start in past.");
        }

        if(newPricingDTO.getStartDate().after(newPricingDTO.getEndDate())) {
            throw new BadTimeRangeException("Start date is after end date.");
        }

        UUID id = UUID.randomUUID();
        int rowsChanged = pricingsRepository.insert(id, newPricingDTO.getStartDate(), newPricingDTO.getEndDate(),
                newPricingDTO.getPrice(), newPricingDTO.getPharmacyId(), newPricingDTO.getMedicineId());
        if(rowsChanged != 1) {
            throw new InternalError();
        }

        Pricings pricings = pricingsRepository.findById(id).orElse(null);
        if(pricings == null) {
            throw new InternalError();
        }

        return pricings;
    }
}
