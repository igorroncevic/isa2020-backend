package team18.pharmacyapp.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MedicineSpecificationDTO {
    private String replacementMedicineCode;

    private int recommendedDose;

    private String contraindications;

    private String drugComposition;

    private String additionalNotes;

}
