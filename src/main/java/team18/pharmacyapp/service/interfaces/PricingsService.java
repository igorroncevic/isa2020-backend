package team18.pharmacyapp.service.interfaces;

import team18.pharmacyapp.model.Pricings;
import team18.pharmacyapp.model.dtos.PricingsDTO;

import java.util.List;
import java.util.UUID;

public interface PricingsService {

    List<PricingsDTO> getAllCurrentPricingsForPhramcy(UUID id);

    List<PricingsDTO> getAllPricingsForMedicine(UUID phId, UUID mId);
}
