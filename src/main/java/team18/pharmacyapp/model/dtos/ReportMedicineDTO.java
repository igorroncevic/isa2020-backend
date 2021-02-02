package team18.pharmacyapp.model.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import team18.pharmacyapp.model.Report;
import team18.pharmacyapp.model.medicine.Medicine;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class ReportMedicineDTO {
    private UUID medicineId;
    private int medicineQuantity;
    private UUID pharmacyId;
}
