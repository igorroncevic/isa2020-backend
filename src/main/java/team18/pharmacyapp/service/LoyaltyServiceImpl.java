package team18.pharmacyapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team18.pharmacyapp.model.Loyalty;
import team18.pharmacyapp.model.dtos.LoyaltyDTO;
import team18.pharmacyapp.model.users.Patient;
import team18.pharmacyapp.repository.LoyaltyRepository;
import team18.pharmacyapp.repository.users.PatientRepository;
import team18.pharmacyapp.service.interfaces.LoyaltyService;

import java.util.List;
import java.util.UUID;

@Service
public class LoyaltyServiceImpl implements LoyaltyService {
    private final LoyaltyRepository loyaltyRepository;
    private final PatientRepository patientRepository;

    @Autowired
    public LoyaltyServiceImpl(LoyaltyRepository loyaltyRepository, PatientRepository patientRepository) {
        this.loyaltyRepository = loyaltyRepository;
        this.patientRepository = patientRepository;
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
        if (loy != null) {
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
    @Transactional
    public void subtractLoyaltyPoints(UUID patientId, int amount) {
        Patient pat = patientRepository.findById(patientId).orElseGet(null);
        if (pat == null) return;

        if (pat.getLoyaltyPoints() - amount >= 0) {
            pat.setLoyaltyPoints(pat.getLoyaltyPoints() - amount);
        } else {
            pat.setLoyaltyPoints(0);
        }

        patientRepository.save(pat);
    }

    @Override
    @Transactional
    public void addLoyaltyPoints(UUID patientId, int amount) {
        Patient pat = patientRepository.findById(patientId).orElseGet(null);
        if (pat == null) return;

        pat.setLoyaltyPoints(pat.getLoyaltyPoints() + amount);

        patientRepository.save(pat);
    }

    @Override
    @Transactional
    public void updatePatientsLoyalty(UUID patientId) {
        Patient pat = patientRepository.findById(patientId).orElseGet(null);
        if (pat == null) return;

        List<Loyalty> loyalties = loyaltyRepository.findAll();
        for (Loyalty l : loyalties) {
            if (pat.getLoyaltyPoints() >= l.getMinPoints() && pat.getLoyaltyPoints() <= l.getMaxPoints()) {
                pat.setLoyalty(l);
                break;
            }
        }

        patientRepository.save(pat);
    }

    @Override
    public Loyalty getPatientsLoyalty(UUID patientId) {
        return loyaltyRepository.getPatientsLoyalty(patientId);
    }


    @Override
    public void deleteById(UUID id) {
        loyaltyRepository.deleteById(id);
    }
}
