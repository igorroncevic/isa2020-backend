package team18.pharmacyapp.model.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LoginPatientDTO {
    private String email;
    private String password;
}
