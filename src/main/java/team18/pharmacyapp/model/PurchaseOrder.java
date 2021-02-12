package team18.pharmacyapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;
import team18.pharmacyapp.model.enums.PurchaseOrderStatus;
import team18.pharmacyapp.model.medicine.PurchaseOrderMedicine;
import team18.pharmacyapp.model.users.PharmacyAdmin;

import javax.persistence.*;
import java.time.LocalDate;
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
    @JsonIgnore
    private PharmacyAdmin pharmacyAdmin;

    @Column(nullable = false)
    private LocalDate endDate;

    @OneToMany(mappedBy = "purchaseOrder", cascade = CascadeType.ALL)
    private List<PurchaseOrderMedicine> purchaseOrderMedicines;

    @Fetch(FetchMode.JOIN)
    @OneToMany(mappedBy = "purchaseOrder", cascade = CascadeType.ALL)
    private List<SupplierPurchaseOrder> supplierPurchaseOrders;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PurchaseOrderStatus status;

}
