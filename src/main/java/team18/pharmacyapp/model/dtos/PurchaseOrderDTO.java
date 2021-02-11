package team18.pharmacyapp.model.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseOrderDTO {

    UUID id;
    PharmacyAdminInfoDTO pharmacyAdmin;
    @JsonFormat(pattern="yyyy-MM-dd")
    Date endDate;
    List<PurchaseOrderMedicineDTO> medicines;

}
