package team18.pharmacyapp.model.medicine;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import team18.pharmacyapp.model.PurchaseOrder;
import team18.pharmacyapp.model.keys.PurchaseOrderMedicineId;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@IdClass(PurchaseOrderMedicineId.class)
public class PurchaseOrderMedicine {
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    private PurchaseOrder purchaseOrder;
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    private Medicine medicine;

    @Column(nullable = false)
    private int quantity;

}
