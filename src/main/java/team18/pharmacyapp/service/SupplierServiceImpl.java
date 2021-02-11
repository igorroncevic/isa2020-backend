package team18.pharmacyapp.service;

import org.springframework.stereotype.Service;
import team18.pharmacyapp.model.Address;
import team18.pharmacyapp.model.dtos.UpdateMyDataDTO;
import team18.pharmacyapp.model.enums.UserRole;
import team18.pharmacyapp.model.users.RegisteredUser;
import team18.pharmacyapp.model.users.Supplier;
import team18.pharmacyapp.repository.AddressRepository;
import team18.pharmacyapp.repository.users.SupplierRepository;
import team18.pharmacyapp.service.interfaces.SupplierService;

import java.util.List;
import java.util.UUID;

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
    public Supplier registerSupplier(RegisteredUser user) {
        Supplier sup = new Supplier();
        sup.setRole(UserRole.supplier);
        sup.setName(user.getName());
        sup.setSurname(user.getSurname());
        sup.setPhoneNumber(user.getPhoneNumber());
        sup.setEmail(user.getEmail());
        sup.setPassword(user.getPassword());
        sup.setAddress(user.getAddress());
        sup = supplierRepository.save(sup);
        supplierRepository.setId(sup.getId(), user.getId());
        return sup;
    }

    @Override
    public Supplier getById(UUID id) {
        return supplierRepository.findById(id).orElse(null);
    }

    @Override
    public Supplier update(UpdateMyDataDTO supplier) {
        Supplier supp = getById(supplier.getId());
        if(supp != null) {
            Address address = addressRepository.findByCountryAndCityAndStreet(supplier.getCountry(), supplier.getCity(), supplier.getStreet());
            if(address == null){
                address = new Address();
                address.setStreet(supplier.getStreet());
                address.setCity(supplier.getCity());
                address.setCountry(supplier.getCountry());
                address = addressRepository.save(address);
            }
            supp.setName(supplier.getName());
            supp.setSurname(supplier.getSurname());
            supp.setPhoneNumber(supplier.getPhoneNumber());
            supp.setAddress(address);
            return supplierRepository.save(supp);
        }

        return null;
    }
}
