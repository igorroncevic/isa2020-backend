package team18.pharmacyapp.model.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class PricingsDTO {

    UUID id;
    String medicine;
    UUID medicineId;
    @JsonFormat(pattern="yyyy-MM-dd")
    LocalDate startDate;
    @JsonFormat(pattern="yyyy-MM-dd")
    LocalDate endDate;
    double price;

}
