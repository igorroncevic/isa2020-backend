package team18.pharmacyapp.model.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import team18.pharmacyapp.model.medicine.Medicine;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class MedicineFilterDTO {
    MedicineDTO medicine;
    List<PharmacyFilteringDTO> pharmacies;
}
