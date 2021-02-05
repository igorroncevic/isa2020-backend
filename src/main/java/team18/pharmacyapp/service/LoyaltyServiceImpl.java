package team18.pharmacyapp.service;

import org.springframework.stereotype.Service;
import team18.pharmacyapp.model.Loyalty;
import team18.pharmacyapp.model.dtos.LoyaltyDTO;
import team18.pharmacyapp.repository.LoyaltyRepository;
import team18.pharmacyapp.service.interfaces.LoyaltyService;

import java.util.List;
import java.util.UUID;

@Service
public class LoyaltyServiceImpl implements LoyaltyService {
    private final LoyaltyRepository loyaltyRepository;

    public LoyaltyServiceImpl(LoyaltyRepository loyaltyRepository) {
        this.loyaltyRepository = loyaltyRepository;
    }

    @Override
    public List<Loyalty> findAll() {
        return loyaltyRepository.findAll();
    }

    @Override
    public Loyalty findOneByCategory(String category) {
        return loyaltyRepository.findByCategory(category);
    }

    @Override
    public Loyalty saveNewLoyalty(LoyaltyDTO newLoyalty) {
        Loyalty loy = new Loyalty();
        loy.setCategory(newLoyalty.getCategory());
        loy.setMinPoints(newLoyalty.getMinPoints());
        loy.setMaxPoints(newLoyalty.getMaxPoints());
        loy.setCheckupPoints(newLoyalty.getCheckupPoints());
        loy.setCounselingPoints(newLoyalty.getCounselingPoints());
        loy.setDiscount(newLoyalty.getDiscount());
        loy = loyaltyRepository.save(loy);
        return loy;
    }

    @Override
    public Loyalty getById(UUID id) {
        return loyaltyRepository.findById(id).orElse(null);
    }

    @Override
    public Loyalty updateLoyalty(Loyalty loyalty) {
        Loyalty loy = getById(loyalty.getId());
        if(loy != null) {
            loy.setCategory(loyalty.getCategory());
            loy.setMinPoints(loyalty.getMinPoints());
            loy.setMaxPoints(loyalty.getMaxPoints());
            loy.setCheckupPoints(loyalty.getCheckupPoints());
            loy.setCounselingPoints(loyalty.getCounselingPoints());
            loy.setDiscount(loyalty.getDiscount());
            return loyaltyRepository.save(loy);
        }
        return null;
    }

    @Override
    public void deleteById(UUID id) {
        loyaltyRepository.deleteById(id);
    }
}
