package team18.pharmacyapp.model.medicine;

import team18.pharmacyapp.model.PurchaseOrder;
import team18.pharmacyapp.model.keys.PurchaseOrderMedicineId;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;

@Entity
@IdClass(PurchaseOrderMedicineId.class)
public class PurchaseOrderMedicine {
    @Id
    @ManyToOne
    private PurchaseOrder purchaseOrder;
    @Id
    @ManyToOne
    private Medicine medicine;
    private int quantity;

    public PurchaseOrderMedicine() {
    }

    public PurchaseOrder getPurchaseOrder() {
        return purchaseOrder;
    }

    public void setPurchaseOrder(PurchaseOrder purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
    }

    public Medicine getMedicine() {
        return medicine;
    }

    public void setMedicine(Medicine medicine) {
        this.medicine = medicine;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
