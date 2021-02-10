package team18.pharmacyapp.model.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class MedicineMarkDTO {
    UUID id;
    String name;
    int loyaltyPoints;
    Float mark;
}
