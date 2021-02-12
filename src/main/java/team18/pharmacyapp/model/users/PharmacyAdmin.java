package team18.pharmacyapp.model.users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import team18.pharmacyapp.model.Pharmacy;
import team18.pharmacyapp.model.PurchaseOrder;

import javax.persistence.*;
import java.util.List;

@Entity(name = "pharmacy_admin")
@Getter
@Setter
@NoArgsConstructor
public class PharmacyAdmin extends RegisteredUser {

    @JsonIgnore
    @OneToMany(mappedBy = "pharmacyAdmin", cascade = CascadeType.ALL)
    private List<PurchaseOrder> purchaseOrders;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false)
    private Pharmacy pharmacy;
}
