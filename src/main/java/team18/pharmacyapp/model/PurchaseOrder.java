package team18.pharmacyapp.model;

import org.hibernate.annotations.GenericGenerator;
import team18.pharmacyapp.model.medicine.PurchaseOrderMedicine;
import team18.pharmacyapp.model.users.PharmacyAdmin;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
public class PurchaseOrder {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;
    @ManyToOne
    private PharmacyAdmin pharmacyAdmin;
    private Date endDate;

    @OneToMany(mappedBy = "purchaseOrder")
    private List<PurchaseOrderMedicine> purchaseOrderMedicines;

    @OneToMany(mappedBy = "purchaseOrder")
    private List<SupplierPurchaseOrder> supplierPurchaseOrders;

    public PurchaseOrder() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public PharmacyAdmin getPharmacyAdmin() {
        return pharmacyAdmin;
    }

    public void setPharmacyAdmin(PharmacyAdmin pharmacyAdmin) {
        this.pharmacyAdmin = pharmacyAdmin;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public List<PurchaseOrderMedicine> getPurchaseOrderMedicines() {
        return purchaseOrderMedicines;
    }

    public void setPurchaseOrderMedicines(List<PurchaseOrderMedicine> purchaseOrderMedicines) {
        this.purchaseOrderMedicines = purchaseOrderMedicines;
    }
}
