package team18.pharmacyapp.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DoctorsPatientDTO {
    private String name;
    private String surname;
    private String mail;
    private String phone;

}
