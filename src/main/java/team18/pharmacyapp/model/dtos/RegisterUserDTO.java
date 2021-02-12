package team18.pharmacyapp.model.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class RegisterUserDTO {
    private String name;
    private String surname;
    private String email;
    private String password;
    private String phoneNumber;
    private String country;
    private String city;
    private String street;
    private String pharmacy;
}
