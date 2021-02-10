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

    void subtractLoyaltyPoints(UUID patientId, int amount);
    void addLoyaltyPoints(UUID patientId, int amount);
    void updatePatientsLoyalty(UUID patientId);
    Loyalty getPatientsLoyalty(UUID patientId);

    void deleteById(UUID id);
}
