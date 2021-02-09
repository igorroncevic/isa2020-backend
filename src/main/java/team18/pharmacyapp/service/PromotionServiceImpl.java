package team18.pharmacyapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team18.pharmacyapp.model.Promotion;
import team18.pharmacyapp.model.dtos.NewPromotionDTO;
import team18.pharmacyapp.model.exceptions.BadTimeRangeException;
import team18.pharmacyapp.repository.PromotionRepository;
import team18.pharmacyapp.service.interfaces.PromotionService;

import java.util.Date;
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

    @Override
    public Promotion addNewPromotion(UUID pharmacyId, NewPromotionDTO newPromotionDTO) throws BadTimeRangeException {
        Date today = new Date();
        if(newPromotionDTO.getStartDate().before(today) || newPromotionDTO.getEndDate().before(today))
            throw new BadTimeRangeException("You can't define promotion for past time");

        UUID promotionId = UUID.randomUUID();

        int rowsAdded = promotionRepository.insert(promotionId, newPromotionDTO.getStartDate(), newPromotionDTO.getEndDate(),
                newPromotionDTO.getText(), pharmacyId);
        if(rowsAdded != 1)
            throw new InternalError("Failed to create promotion.");

        Promotion promotion = promotionRepository.findById(promotionId).orElse(null);
        return promotion;
    }
}
