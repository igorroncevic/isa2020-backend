package team18.pharmacyapp.service.interfaces;

import team18.pharmacyapp.model.Promotion;
import team18.pharmacyapp.model.dtos.NewPromotionDTO;
import team18.pharmacyapp.model.exceptions.BadTimeRangeException;

import java.util.List;
import java.util.UUID;

public interface PromotionService {


    List<Promotion> getAllPromotionsForPharmacy(UUID id);

    Promotion addNewPromotion(UUID pharmacyId, NewPromotionDTO newPromotionDTO) throws BadTimeRangeException;
}
