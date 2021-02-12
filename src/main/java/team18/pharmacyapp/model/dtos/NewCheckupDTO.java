package team18.pharmacyapp.model.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NewCheckupDTO {

    UUID doctorId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    Date startTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    Date endTime;
    double price;

}
