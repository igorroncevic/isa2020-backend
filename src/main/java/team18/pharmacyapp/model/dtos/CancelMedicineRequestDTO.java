package team18.pharmacyapp.model.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class CancelMedicineRequestDTO {
    private UUID patientId;
    private UUID reservationId;
    private UUID medicineId;
    private UUID pharmacyId;
}
