package team18.pharmacyapp.model.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import team18.pharmacyapp.model.Address;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class PharmacyFilteringDTO {
    UUID id;
    String name;
    Address address;
    Double averageMark;

    public PharmacyFilteringDTO(UUID id, String name, Address address, Double averageMark) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.averageMark = averageMark;
    }
}
