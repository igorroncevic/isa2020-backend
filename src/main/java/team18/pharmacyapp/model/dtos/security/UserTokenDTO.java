package team18.pharmacyapp.model.dtos.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import team18.pharmacyapp.model.enums.UserRole;

import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class UserTokenDTO {
    private String accessToken;
    private int expiresIn;
    private UUID userId;
    private UserRole userRole;
    private String name;
    private String surname;
    private String email;
}
