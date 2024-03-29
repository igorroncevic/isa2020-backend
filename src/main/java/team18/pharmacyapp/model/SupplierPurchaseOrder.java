package team18.pharmacyapp.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import team18.pharmacyapp.model.keys.SupplierPurchaseOrderId;
import team18.pharmacyapp.model.users.Supplier;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@Entity(name = "supplier_purchase_order")
@Getter
@Setter
@NoArgsConstructor
@IdClass(SupplierPurchaseOrderId.class)
public class SupplierPurchaseOrder {
    @Id
    @ManyToOne
    private Supplier supplier;
    @Id
    @ManyToOne(cascade = CascadeType.ALL)
    private PurchaseOrder purchaseOrder;
    @Column(nullable = false)
    private double price;
    @Column(nullable = false)
    private LocalDate deliveryDate;
    @Column(nullable = false)
    private boolean accepted;

}
