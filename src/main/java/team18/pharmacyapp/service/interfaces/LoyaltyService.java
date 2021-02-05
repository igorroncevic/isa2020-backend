package team18.pharmacyapp.service.interfaces;

import team18.pharmacyapp.model.Loyalty;
import team18.pharmacyapp.model.dtos.LoyaltyDTO;

import java.util.List;
import java.util.UUID;

public interface LoyaltyService {
    List<Loyalty> findAll();

    Loyalty findOneByCategory(String category);

    Loyalty saveNewLoyalty(LoyaltyDTO newLoyalty);

    Loyalty getById(UUID id);

    Loyalty updateLoyalty(Loyalty loyalty);

    void deleteById(UUID id);
}
