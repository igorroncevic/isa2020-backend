package team18.pharmacyapp.model.medicine;

import team18.pharmacyapp.model.keys.SupplierMedicineId;
import team18.pharmacyapp.model.medicine.Medicine;
import team18.pharmacyapp.model.users.Supplier;

import javax.persistence.*;

@Entity
@IdClass(SupplierMedicineId.class)
public class SupplierMedicine {
    @Id
    @ManyToOne
    private Supplier supplier;
    @Id
    @ManyToOne
    private Medicine medicine;
    private int quantity;

    public SupplierMedicine() {
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
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
