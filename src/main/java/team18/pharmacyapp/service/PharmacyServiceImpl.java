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
        return pharmacyRepository.filterAll(name, (double)mark, city);
        //List<Pharmacy> pharmacies = pharmacyRepository.filterAll(name, (double)mark, city);
//        List<PharmacyFilteringDTO> pharmacyFilteringDTOs = new ArrayList<>();
//
//        for(Pharmacy p : pharmacies){
//            Float averageMark = pharmacyRepository.getAverageMark(p.getId());
//            pharmacyFilteringDTOs.add(new PharmacyFilteringDTO(p.getId(), p.getName(), p.getAddress(), averageMark));
//        }
//
//        return pharmacyFilteringDTOs;
    }

}
