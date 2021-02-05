package team18.pharmacyapp.model.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import team18.pharmacyapp.model.enums.TermType;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class DoctorScheduleTermDTO {
    private UUID doctorId;
    private UUID patientId;
    private UUID pharmacyId;
    private Date startTime;
    private Date endTime;
    private TermType type;
}
