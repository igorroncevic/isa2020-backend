package team18.pharmacyapp.model.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import team18.pharmacyapp.model.Address;
import team18.pharmacyapp.model.enums.UserRole;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class DoctorDTO {
    private UUID id;
    private String name;
    private String surname;
    private String email;
    private String phoneNumber;
    private UserRole role;
    private Address address;
}
