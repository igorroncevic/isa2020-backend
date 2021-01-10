package team18.pharmacyapp.model.users;

import team18.pharmacyapp.model.Pharmacy;
import team18.pharmacyapp.model.PurchaseOrder;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.List;

@Entity
public class PharmacyAdmin extends User {

    @OneToMany(mappedBy = "pharmacyAdmin")
    private List<PurchaseOrder> purchaseOrders;

    @ManyToOne()
    private Pharmacy pharmacy;
}
