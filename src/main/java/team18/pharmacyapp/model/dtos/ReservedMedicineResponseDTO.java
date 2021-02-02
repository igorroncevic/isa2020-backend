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
public class ReservedMedicineResponseDTO {
    private UUID id;
    private UUID patientId;
    private UUID medicineId;
    private UUID pharmacyId;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date pickupDate;
    private String email;
    private String medicineName;
    private String patientName;
    private String patientSurname;
    private boolean handled;

    public ReservedMedicineResponseDTO(UUID id, UUID patientId, UUID medicineId, UUID pharmacyId, Date pickupDate, String email,String medicineName,String patientName,String patientSurname,boolean handled) {
        this.id = id;
        this.patientId = patientId;
        this.medicineId = medicineId;
        this.pharmacyId = pharmacyId;
        this.pickupDate = pickupDate;
        this.email = email;
        this.medicineName=medicineName;
        this.patientName=patientName;
        this.patientSurname=patientSurname;
        this.handled=handled;
    }
}
