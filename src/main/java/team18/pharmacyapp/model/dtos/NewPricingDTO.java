package team18.pharmacyapp.model.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class NewPricingDTO {

    UUID medicineId;
    UUID pharmacyId;
    LocalDate startDate;
    LocalDate endDate;
    double price;

}
