package team18.pharmacyapp.model.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class MarkDTO {
    int markValue;
    UUID doctorId;
    UUID medicineId;
    UUID patientId;
    UUID pharmacyId;
}
