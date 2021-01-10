package team18.pharmacyapp.model.keys;

import java.io.Serializable;
import java.util.UUID;

public class SupplierPurchaseOrderId implements Serializable {
    UUID supplier;
    UUID purchaseOrder;
}
