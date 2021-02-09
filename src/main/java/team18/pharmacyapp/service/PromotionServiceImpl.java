package team18.pharmacyapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team18.pharmacyapp.model.Promotion;
import team18.pharmacyapp.model.dtos.PharmacyDTO;
import team18.pharmacyapp.model.dtos.PromotionDTO;
import team18.pharmacyapp.repository.PromotionRepository;
import team18.pharmacyapp.service.interfaces.PromotionService;

import java.util.ArrayList;
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
    public List<PromotionDTO> getPatientsPromotions(UUID id) {
        List<Promotion> promotions = promotionRepository.getPatientsPromotions(id);
        List<PromotionDTO>finalPromotions = new ArrayList<>();

        for(Promotion p : promotions){
            finalPromotions.add(new PromotionDTO(p.getId(), p.getStartDate(), p.getEndDate(),
                    new PharmacyDTO(p.getPharmacy().getId(), p.getPharmacy().getName(), p.getPharmacy().getAddress().getStreet(),
                                    p.getPharmacy().getAddress().getCity(), p.getPharmacy().getAddress().getCountry()),
                    p.getText()));
        }

        return finalPromotions;
    }
}
