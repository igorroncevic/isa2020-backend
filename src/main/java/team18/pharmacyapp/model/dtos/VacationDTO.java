package team18.pharmacyapp.model.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import team18.pharmacyapp.model.enums.VacationStatus;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class VacationDTO {

    private UUID id;

    @JsonFormat(pattern="yyyy-MM-dd")
    private Date startDate;

    @JsonFormat(pattern="yyyy-MM-dd")
    private Date endDate;

    private DoctorDTO doctor;

    private VacationStatus status;

    private String rejectionReason;

}
