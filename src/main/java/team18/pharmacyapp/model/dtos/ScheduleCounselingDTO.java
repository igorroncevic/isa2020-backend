package team18.pharmacyapp.model.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class ScheduleCounselingDTO {
    UUID patientId;
    UUID doctorId;
    Date fromTime;
    Date toTime;
}
