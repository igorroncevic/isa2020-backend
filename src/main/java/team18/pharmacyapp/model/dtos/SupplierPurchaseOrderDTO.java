package team18.pharmacyapp.model.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import team18.pharmacyapp.model.enums.PurchaseOrderStatus;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SupplierPurchaseOrderDTO {
    private UUID purchaseOrderId;
    private PurchaseOrderStatus purchaseOrderStatus;
    private List<PurchaseOrderMedicineDTO> medicines;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date deliveryDate;
    private double price;
    private UUID supplierId;
}
