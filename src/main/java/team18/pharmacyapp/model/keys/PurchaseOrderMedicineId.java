package team18.pharmacyapp.model.keys;

import java.io.Serializable;
import java.util.UUID;

public class PurchaseOrderMedicineId implements Serializable {
    UUID purchaseOrder;
    UUID medicine;
}
