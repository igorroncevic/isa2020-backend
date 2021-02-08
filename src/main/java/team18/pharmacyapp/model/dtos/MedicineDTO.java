package team18.pharmacyapp.model.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MedicineDTO {
    private String medicineName;
    private String medicineCode;
    private String medicineType;
    private String medicineForm;
    private String medicineManufacturer;
    private String issuingRegime;
    private int loyaltyPoints;
    private String replacementMedicine;
    private int recommendedDose;
    private String contraindications;
    private String drugComposition;
    private String additionalNotes;
}
