package team18.pharmacyapp.model.dtos.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ChangePassDTO {
    private String email;
    private String oldPass;
    private String newPass;
}
