package team18.pharmacyapp.service.interfaces;

import team18.pharmacyapp.model.Pharmacy;
import team18.pharmacyapp.model.dtos.PharmacyFilteringDTO;
import team18.pharmacyapp.model.dtos.PharmacyMarkPriceDTO;

import java.util.List;
import java.util.UUID;

public interface PharmacyService {

    List<Pharmacy> getAll();

    Pharmacy getById(UUID id);

    List<PharmacyFilteringDTO> getAllFiltered(String name, Float mark, String city);

    Float getAverageMark(UUID id);

    List<PharmacyMarkPriceDTO> getAllPatientsPharmacies(UUID id);
}
