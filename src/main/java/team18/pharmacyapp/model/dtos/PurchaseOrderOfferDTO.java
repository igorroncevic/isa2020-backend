package team18.pharmacyapp.model.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseOrderOfferDTO {

    UserInfoDTO supplier;
    @JsonFormat(pattern="yyyy-MM-dd")
    LocalDate deliveryDate;
    double price;

}
