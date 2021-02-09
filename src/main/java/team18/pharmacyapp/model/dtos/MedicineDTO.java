package team18.pharmacyapp.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MedicineDTO {
    private UUID id;
    private String name;
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
