package team18.pharmacyapp.model.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class ReserveMedicineDTO {
    private UUID patient_id;
    private UUID medicine_id;
    private Date pickupDate;
}
