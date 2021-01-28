package team18.pharmacyapp.model.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import team18.pharmacyapp.model.Pharmacy;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class DoctorDTO {

    private String name;
    private String surname;
    private Float averageMark;
    private List<String> pharmacies;
}
