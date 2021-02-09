package team18.pharmacyapp.service.interfaces;

import team18.pharmacyapp.model.dtos.PromotionDTO;

import java.util.List;
import java.util.UUID;

public interface PromotionService {
    List<PromotionDTO> getPatientsPromotions(UUID id);
}
