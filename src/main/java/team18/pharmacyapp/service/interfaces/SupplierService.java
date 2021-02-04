package team18.pharmacyapp.service.interfaces;

import team18.pharmacyapp.model.dtos.RegisterUserDTO;
import team18.pharmacyapp.model.users.Supplier;

import java.util.List;

public interface SupplierService {

    List<Supplier> getAll();

    Supplier registerNewSupplier(RegisterUserDTO supplier);

}
