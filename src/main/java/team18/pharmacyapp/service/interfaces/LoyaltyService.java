package team18.pharmacyapp.service.interfaces;

import team18.pharmacyapp.model.Loyalty;

import java.util.List;

public interface LoyaltyService {
    List<Loyalty> findAll();
    Loyalty findOneByCategory(String category);
}
