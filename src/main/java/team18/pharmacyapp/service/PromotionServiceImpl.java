package team18.pharmacyapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team18.pharmacyapp.model.Promotion;
import team18.pharmacyapp.repository.PromotionRepository;
import team18.pharmacyapp.service.interfaces.PromotionService;

import java.util.List;
import java.util.UUID;

@Service
public class PromotionServiceImpl implements PromotionService {

    private final PromotionRepository promotionRepository;

    @Autowired
    public PromotionServiceImpl(PromotionRepository promotionRepository) {
        this.promotionRepository = promotionRepository;
    }

    @Override
    public List<Promotion> getAllPromotionsForPharmacy(UUID id) {
        return promotionRepository.getAllForPharmacy(id);
    }
}
