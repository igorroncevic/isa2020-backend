package team18.pharmacyapp.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseOrderMedicineDTO {

    UUID medicineId;
    String medicineName;
    int orderQuantity;

    public PurchaseOrderMedicineDTO(String medicineName, int orderQuantity) {
        this.medicineName = medicineName;
        this.orderQuantity = orderQuantity;
    }
}
