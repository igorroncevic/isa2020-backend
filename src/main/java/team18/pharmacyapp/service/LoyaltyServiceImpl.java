package team18.pharmacyapp.service;

import team18.pharmacyapp.model.Loyalty;
import team18.pharmacyapp.repository.LoyaltyRepository;
import team18.pharmacyapp.service.interfaces.LoyaltyService;

import java.util.List;

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
}
