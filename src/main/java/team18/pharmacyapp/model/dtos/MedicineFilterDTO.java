package team18.pharmacyapp.model.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class MedicineFilterDTO {
    MedicineDTO medicine;
    List<PharmacyFilteringDTO> pharmacies;
}
