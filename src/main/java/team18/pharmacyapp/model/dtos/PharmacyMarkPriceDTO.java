package team18.pharmacyapp.model.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class PharmacyMarkPriceDTO {
    UUID id;
    String name;
    String street;
    String city;
    String country;
    double minPrice;
    double maxPrice;
    Double mark;

    public PharmacyMarkPriceDTO(UUID id, String name, String street, String city, String country, double minPrice, double maxPrice, Double mark) {
        this.id = id;
        this.name = name;
        this.street = street;
        this.city = city;
        this.country = country;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.mark = mark;
    }
}
