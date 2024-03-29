package team18.pharmacyapp.model.users;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import team18.pharmacyapp.model.SupplierPurchaseOrder;
import team18.pharmacyapp.model.medicine.SupplierMedicine;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Supplier extends RegisteredUser {
    @OneToMany(mappedBy = "supplier", cascade = CascadeType.ALL)
    private List<SupplierMedicine> supplierMedicineList;

    @OneToMany(mappedBy = "supplier", cascade = CascadeType.ALL)
    private List<SupplierPurchaseOrder> supplierPurchaseOrders;

}
