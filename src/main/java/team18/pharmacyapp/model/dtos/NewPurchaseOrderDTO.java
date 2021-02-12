package team18.pharmacyapp.model.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class NewPurchaseOrderDTO {

    UUID pharmacyAdminId;
    @JsonFormat(pattern="yyyy-MM-dd")
    LocalDate endDate;
    List<PurchaseOrderMedicineDTO> medicines;

}
