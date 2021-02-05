package team18.pharmacyapp.model.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class LoyaltyDTO {
    private String category;
    private int minPoints;
    private int maxPoints;
    private double discount;
    private int checkupPoints;
    private int counselingPoints;
}
