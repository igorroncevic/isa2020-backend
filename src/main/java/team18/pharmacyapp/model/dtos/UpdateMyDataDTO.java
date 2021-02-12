package team18.pharmacyapp.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateMyDataDTO {
    private UUID id;
    private String name;
    private String surname;
    private String phoneNumber;
    private String country;
    private String city;
    private String street;
}
