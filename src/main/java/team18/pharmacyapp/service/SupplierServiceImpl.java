package team18.pharmacyapp.service;

import org.springframework.stereotype.Service;
import team18.pharmacyapp.model.Address;
import team18.pharmacyapp.model.dtos.RegisterUserDTO;
import team18.pharmacyapp.model.enums.UserRole;
import team18.pharmacyapp.model.users.Supplier;
import team18.pharmacyapp.repository.AddressRepository;
import team18.pharmacyapp.repository.SupplierRepository;
import team18.pharmacyapp.service.interfaces.SupplierService;

import java.util.List;

@Service
public class SupplierServiceImpl implements SupplierService {
    private final SupplierRepository supplierRepository;
    private final AddressRepository addressRepository;

    public SupplierServiceImpl(SupplierRepository supplierRepository, AddressRepository addressRepository) {
        this.supplierRepository = supplierRepository;
        this.addressRepository = addressRepository;
    }

    @Override
    public List<Supplier> getAll() {
        return supplierRepository.findAll();
    }

    @Override
    public Supplier registerNewSupplier(RegisterUserDTO supplier) {
        Supplier sup = new Supplier();
        Address address=addressRepository.findByCountryAndCityAndStreet(supplier.getCountry(), supplier.getCity(), supplier.getStreet());
        if(address == null){
            address = new Address();
            address.setStreet(supplier.getStreet());
            address.setCity(supplier.getCity());
            address.setCountry(supplier.getCountry());
            address = addressRepository.save(address);
        }
        sup.setRole(UserRole.supplier);
        sup.setName(supplier.getName());
        sup.setSurname(supplier.getSurname());
        sup.setPhoneNumber(supplier.getPhoneNumber());
        sup.setEmail(supplier.getEmail());
        sup.setPassword(supplier.getPassword());
        sup.setAddress(address);
        sup = supplierRepository.save(sup);

        return sup;
    }
}
