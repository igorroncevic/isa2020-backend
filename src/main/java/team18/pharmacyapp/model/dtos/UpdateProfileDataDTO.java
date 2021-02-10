package team18.pharmacyapp.model.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class UpdateProfileDataDTO {
    UUID id;
    String name;
    String surname;
    String phoneNumber;
    String newPassword;
    String confirmPassword;
}
