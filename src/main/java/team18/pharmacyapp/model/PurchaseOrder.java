package team18.pharmacyapp.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import team18.pharmacyapp.model.medicine.PurchaseOrderMedicine;
import team18.pharmacyapp.model.users.PharmacyAdmin;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity (name = "purchase_order")
@Getter
@Setter
@NoArgsConstructor
public class PurchaseOrder {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID", strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private PharmacyAdmin pharmacyAdmin;

    @Column(nullable = false)
    private Date endDate;

    @OneToMany(mappedBy = "purchaseOrder", cascade = CascadeType.ALL)
    private List<PurchaseOrderMedicine> purchaseOrderMedicines;

    @OneToMany(mappedBy = "purchaseOrder", cascade = CascadeType.ALL)
    private List<SupplierPurchaseOrder> supplierPurchaseOrders;

}
