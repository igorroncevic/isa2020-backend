package team18.pharmacyapp.model.dtos;

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
public class PatientDTO {
    private UUID id;
    private String name;
    private String surname;
    private String email;
    private String phoneNumber;
    private UserRole role;
    private Address address;
    private int loyaltyPoints;
    private LoyaltyDTO loyalty;
    private int penalties;
}
