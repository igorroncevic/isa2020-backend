package team18.pharmacyapp.service.interfaces;

import team18.pharmacyapp.model.Promotion;

import java.util.List;
import java.util.UUID;

public interface PromotionService {
    List<Promotion> getPatientsPromotions(UUID id);
}
