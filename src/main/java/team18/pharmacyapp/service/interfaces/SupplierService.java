package team18.pharmacyapp.service.interfaces;

import team18.pharmacyapp.model.dtos.RegisterUserDTO;
import team18.pharmacyapp.model.dtos.UpdateMyDataDTO;
import team18.pharmacyapp.model.users.RegisteredUser;
import team18.pharmacyapp.model.users.Supplier;

import java.util.List;
import java.util.UUID;

public interface SupplierService {

    List<Supplier> getAll();

    Supplier registerSupplier(RegisteredUser user);

    Supplier getById(UUID id);

    Supplier update(UpdateMyDataDTO supplier);
}
