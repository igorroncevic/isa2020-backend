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
public class ComplaintDTO {
    private UUID complaintId;
    private UUID patientId;
    private String complaintText;
    private String complaintResponse;
    private UUID doctorId;
    private UUID pharmacyId;
}
