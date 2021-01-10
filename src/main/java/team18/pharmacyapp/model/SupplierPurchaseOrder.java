package team18.pharmacyapp.model;

import team18.pharmacyapp.model.keys.SupplierPurchaseOrderId;
import team18.pharmacyapp.model.users.Supplier;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;
import java.util.Date;

@Entity
@IdClass(SupplierPurchaseOrderId.class)
public class SupplierPurchaseOrder {
    @Id
    @ManyToOne
    private Supplier supplier;
    @Id
    @ManyToOne
    private PurchaseOrder purchaseOrder;
    private double price;
    private Date deliveryDate;

    public SupplierPurchaseOrder() {
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public PurchaseOrder getPurchaseOrder() {
        return purchaseOrder;
    }

    public void setPurchaseOrder(PurchaseOrder purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }
}
