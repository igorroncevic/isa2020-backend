package team18.pharmacyapp.service;

import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team18.pharmacyapp.model.Pricings;
import team18.pharmacyapp.model.dtos.NewPricingDTO;
import team18.pharmacyapp.model.dtos.PricingsDTO;
import team18.pharmacyapp.model.dtos.UpdatePricingDTO;
import team18.pharmacyapp.model.exceptions.ActionNotAllowedException;
import team18.pharmacyapp.model.exceptions.BadTimeRangeException;
import team18.pharmacyapp.repository.PricingsRepository;
import team18.pharmacyapp.service.interfaces.PricingsService;

import java.time.LocalDate;
import java.util.ArrayList;
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
        List<Pricings> pricings = pricingsRepository.getAllCurrentPricingsForPharmacy(id, LocalDate.now());
        List<PricingsDTO> pricingsDTOs = new ArrayList<>();
        for (Pricings pricing : pricings) {
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
        for (Pricings pricing : pricings) {
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
        if (pricings == null) {
            throw new NotFoundException("Pricing not found");
        }

        LocalDate today = LocalDate.now();
        if (!pricings.getStartDate().isAfter(today)) {
            throw new ActionNotAllowedException("You can't delete pricing that have passed or is currently active.");
        }

        pricingsRepository.deleteById(id);
    }

    @Override
    public Pricings addNewPricing(NewPricingDTO newPricingDTO) throws BadTimeRangeException {

        int overlappingPricings = pricingsRepository.getNumberOfOverlappingPricings(newPricingDTO.getStartDate(), newPricingDTO.getEndDate(),
                newPricingDTO.getPharmacyId(), newPricingDTO.getMedicineId());
        if (overlappingPricings != 0) {
            throw new BadTimeRangeException("Pricing can't overlap with other pricings");
        }

        if (newPricingDTO.getStartDate().isBefore(LocalDate.now())) {
            throw new BadTimeRangeException("You can't create pricing that start in past.");
        }

        if (newPricingDTO.getStartDate().isAfter(newPricingDTO.getEndDate())) {
            throw new BadTimeRangeException("Start date is after end date.");
        }

        UUID id = UUID.randomUUID();
        int rowsChanged = pricingsRepository.insert(id, newPricingDTO.getStartDate(), newPricingDTO.getEndDate(),
                newPricingDTO.getPrice(), newPricingDTO.getPharmacyId(), newPricingDTO.getMedicineId());
        if (rowsChanged != 1) {
            throw new InternalError();
        }

        Pricings pricings = pricingsRepository.findById(id).orElse(null);
        if (pricings == null) {
            throw new InternalError();
        }

        return pricings;
    }

    @Override
    public void updatePricing(UUID id, UpdatePricingDTO updatePricingDTO) throws NotFoundException, ActionNotAllowedException, BadTimeRangeException {
        Pricings ogPricing = pricingsRepository.findById(id).orElse(null);

        if (ogPricing == null) {
            throw new NotFoundException("This pricing does not exists.");
        }

        LocalDate today = LocalDate.now();
        if (ogPricing.getEndDate().isBefore(today)) {
            throw new ActionNotAllowedException("You can't modify pricings that have expired");
        } else if ((ogPricing.getStartDate().isBefore(today) || ogPricing.getStartDate().equals(today)) && !ogPricing.getStartDate().equals(updatePricingDTO.getStartDate())) {
            throw new ActionNotAllowedException("You can't modify start date of pricings that have started");
        } else if ((ogPricing.getStartDate().isBefore(today) || ogPricing.getStartDate().equals(today)) && ogPricing.getPrice() != updatePricingDTO.getPrice()) {
            throw new ActionNotAllowedException("You can't modify price of pricings that have started");
        } else if (ogPricing.getStartDate().isAfter(today) && updatePricingDTO.getStartDate().isBefore(today)) {
            throw new BadTimeRangeException("You can't update pricings to start in past.");
        } else if (updatePricingDTO.getEndDate().isBefore(today)) {
            throw new BadTimeRangeException("You can't update pricings to end in past.");
        } else if (updatePricingDTO.getStartDate().isAfter(updatePricingDTO.getEndDate())) {
            throw new BadTimeRangeException("Start date is after end date.");
        }

        int overlappingPricings = pricingsRepository.getNumberOfOverlappingPricings(updatePricingDTO.getStartDate(), updatePricingDTO.getEndDate(), id);
        if (overlappingPricings != 1) {
            throw new BadTimeRangeException("Pricing can't overlap with other pricings");
        }

        ogPricing.setStartDate(updatePricingDTO.getStartDate());
        ogPricing.setEndDate(updatePricingDTO.getEndDate());
        ogPricing.setPrice(updatePricingDTO.getPrice());

        pricingsRepository.save(ogPricing);

    }
}
