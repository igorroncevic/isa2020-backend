package team18.pharmacyapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team18.pharmacyapp.model.Address;
import team18.pharmacyapp.model.Pharmacy;
import team18.pharmacyapp.model.dtos.PharmacyFilteringDTO;
import team18.pharmacyapp.model.dtos.PharmacyMarkPriceDTO;
import team18.pharmacyapp.repository.AddressRepository;
import team18.pharmacyapp.repository.MarkRepository;
import team18.pharmacyapp.repository.PharmacyRepository;
import team18.pharmacyapp.service.interfaces.PharmacyService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class PharmacyServiceImpl implements PharmacyService {
    private final PharmacyRepository pharmacyRepository;
    private final AddressRepository addressRepository;
    private final MarkRepository markRepository;

    @Autowired
    public PharmacyServiceImpl(PharmacyRepository pharmacyRepository, AddressRepository addressRepository, MarkRepository markRepository) {
        this.pharmacyRepository = pharmacyRepository;
        this.addressRepository = addressRepository;
        this.markRepository = markRepository;
    }

    @Override
    public List<Pharmacy> getAll() {
        return pharmacyRepository.findAll();
    }

    @Override
    public Pharmacy getById(UUID id) {
        return pharmacyRepository.findById(id).get();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PharmacyMarkPriceDTO> getAllPatientsPharmaciesOptimized(UUID id) {
        List<Pharmacy> pTerms = pharmacyRepository.getPatientsPharmacies(id);

        List<PharmacyMarkPriceDTO> pFinal = new ArrayList<>();
        for (Pharmacy ph : pTerms) {
            PharmacyMarkPriceDTO phDTO = new PharmacyMarkPriceDTO();
            phDTO.setId(ph.getId());
            phDTO.setName(ph.getName());
            phDTO.setStreet(ph.getAddress().getStreet());
            phDTO.setCity(ph.getAddress().getCity());
            phDTO.setCountry(ph.getAddress().getCountry());
            Float averageMark = markRepository.getAverageMarkForPharmacy(ph.getId());
            phDTO.setMark((double) averageMark);
            pFinal.add(phDTO);
        }

        return pFinal;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PharmacyFilteringDTO> getAllFiltered(String name, Float mark, String city) {
        List<PharmacyFilteringDTO> pharmacies = pharmacyRepository.findAllForFiltering();
        List<PharmacyFilteringDTO> finalPharmacies = new ArrayList<>();

        for (PharmacyFilteringDTO p : pharmacies) {
            boolean continueFlag = false;
            if (name != null) {
                if (!p.getName().toUpperCase().contains(name.toUpperCase())) continueFlag = true;
            }

            if (city != null) {
                if (!p.getAddress().getCity().toUpperCase().contains(city.toUpperCase())) continueFlag = true;
            }

            if (mark != null) {
                if (p.getAverageMark() < mark) continueFlag = true;
            }

            if (continueFlag) continue;

            finalPharmacies.add(p);
        }

        return finalPharmacies;
    }

    @Override
    public Pharmacy registerNewPharmacy(team18.pharmacyapp.model.dtos.PharmacyDTO pharmacy) {
        Pharmacy newPharmacy = new Pharmacy();
        Address address = addressRepository.findByCountryAndCityAndStreet(pharmacy.getCountry(), pharmacy.getCity(), pharmacy.getStreet());
        if (address == null) {
            address = new Address();
            address.setStreet(pharmacy.getStreet());
            address.setCity(pharmacy.getCity());
            address.setCountry(pharmacy.getCountry());
            address = addressRepository.save(address);
        }
        newPharmacy.setName(pharmacy.getName());
        newPharmacy.setAddress(address);
        newPharmacy = pharmacyRepository.save(newPharmacy);
        return newPharmacy;
    }
}
