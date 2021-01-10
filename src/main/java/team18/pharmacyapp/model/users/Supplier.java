package team18.pharmacyapp.model.users;

import team18.pharmacyapp.model.medicine.SupplierMedicine;
import team18.pharmacyapp.model.SupplierPurchaseOrder;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
public class Supplier extends User {
    @OneToMany(mappedBy = "supplier")
    private List<SupplierMedicine> supplierMedicineList;

    @OneToMany(mappedBy = "supplier")
    private List<SupplierPurchaseOrder> supplierPurchaseOrders;


    public List<SupplierMedicine> getSupplierMedicineList() {
        return supplierMedicineList;
    }

    public void setSupplierMedicineList(List<SupplierMedicine> supplierMedicineList) {
        this.supplierMedicineList = supplierMedicineList;
    }
}
