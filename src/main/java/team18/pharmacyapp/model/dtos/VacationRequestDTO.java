package team18.pharmacyapp.model.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class VacationRequestDTO {
    private Date startDate;
    private Date endDate;
    private UUID doctorId;
}
