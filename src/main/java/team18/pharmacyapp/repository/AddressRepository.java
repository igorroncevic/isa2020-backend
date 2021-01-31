package team18.pharmacyapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team18.pharmacyapp.model.Address;

import java.util.UUID;

public interface AddressRepository extends JpaRepository<Address, UUID> {
    public Address findByCountryAndCityAndStreet(String country,String city,String street);
}
