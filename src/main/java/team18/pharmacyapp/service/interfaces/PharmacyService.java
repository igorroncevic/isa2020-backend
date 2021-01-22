package team18.pharmacyapp.service.interfaces;

import team18.pharmacyapp.model.Pharmacy;

import java.util.List;
import java.util.UUID;

public interface PharmacyService {

    List<Pharmacy> getAll();

    Pharmacy getById(UUID id);

}
