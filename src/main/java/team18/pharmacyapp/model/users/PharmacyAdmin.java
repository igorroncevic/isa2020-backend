package team18.pharmacyapp.model.users;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import team18.pharmacyapp.model.Pharmacy;
import team18.pharmacyapp.model.PurchaseOrder;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class PharmacyAdmin extends User {

    @OneToMany(mappedBy = "pharmacyAdmin",cascade = CascadeType.ALL)
    private List<PurchaseOrder> purchaseOrders;

    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Pharmacy pharmacy;
}
