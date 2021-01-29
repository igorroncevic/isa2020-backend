package team18.pharmacyapp.service;

import org.springframework.stereotype.Service;
import team18.pharmacyapp.model.Pharmacy;
import team18.pharmacyapp.model.dtos.PharmacyFilteringDTO;
import team18.pharmacyapp.repository.PharmacyRepository;
import team18.pharmacyapp.service.interfaces.PharmacyService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class PharmacyServiceImpl implements PharmacyService {

    private final PharmacyRepository pharmacyRepository;

    public PharmacyServiceImpl(PharmacyRepository pharmacyRepository) {
        this.pharmacyRepository = pharmacyRepository;
    }

    @Override
    public List<Pharmacy> getAll() {
        return pharmacyRepository.findAll();
    }

    @Override
    public Pharmacy getById(UUID id) {
        return pharmacyRepository.findById(id).get();
    }

    public Float getAverageMark(UUID id){
        return pharmacyRepository.getAverageMark(id);
    }

    @Override
    public List<PharmacyFilteringDTO> getAllFiltered(String name, Float mark, String city) {
        List<PharmacyFilteringDTO> pharmacies = pharmacyRepository.findAllForFiltering();
        List<PharmacyFilteringDTO> finalPharmacies = new ArrayList<>();

        for(PharmacyFilteringDTO p : pharmacies){
            boolean continueFlag = false;
            if(name != null) {
                if(!p.getName().toUpperCase().contains(name.toUpperCase())) continueFlag = true;
            }

            if(city != null){
                if(!p.getAddress().getCity().toUpperCase().contains(city.toUpperCase())) continueFlag = true;
            }

            if(mark != null){
                if(p.getAverageMark() < mark) continueFlag = true;
            }

            if(continueFlag) continue;

            finalPharmacies.add(p);
        }

        return finalPharmacies;
    }

}
