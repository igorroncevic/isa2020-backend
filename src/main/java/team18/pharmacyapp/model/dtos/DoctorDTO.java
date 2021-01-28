package team18.pharmacyapp.model.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import team18.pharmacyapp.model.Pharmacy;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class DoctorDTO {
    private UUID id;
    private String name;
    private String surname;
    private Float averageMark;
    private List<Pharmacy> pharmacies;
}
