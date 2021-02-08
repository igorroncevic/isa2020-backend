package team18.pharmacyapp.service.interfaces;

import javassist.NotFoundException;
import team18.pharmacyapp.model.Pricings;
import team18.pharmacyapp.model.dtos.NewPricingDTO;
import team18.pharmacyapp.model.dtos.PricingsDTO;
import team18.pharmacyapp.model.dtos.UpdatePricingDTO;
import team18.pharmacyapp.model.exceptions.ActionNotAllowedException;
import team18.pharmacyapp.model.exceptions.BadTimeRangeException;

import java.util.List;
import java.util.UUID;

public interface PricingsService {

    List<PricingsDTO> getAllCurrentPricingsForPhramcy(UUID id);

    List<PricingsDTO> getAllPricingsForMedicine(UUID phId, UUID mId);

    void deletePricing(UUID id) throws NotFoundException, ActionNotAllowedException;

    Pricings addNewPricing(NewPricingDTO newPricingDTO) throws BadTimeRangeException;

    void updatePricing(UUID id, UpdatePricingDTO updatePricingDTO) throws NotFoundException, ActionNotAllowedException, BadTimeRangeException;
}
