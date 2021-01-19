package team18.pharmacyapp.model.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class ReserveMedicineRequestDTO {
    private UUID patient_id;
    private UUID medicine_id;
    private UUID pharmacy_id;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date pickupDate;
}
