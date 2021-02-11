package team18.pharmacyapp.service.interfaces;

import team18.pharmacyapp.model.dtos.SupplierPurchaseOrderDTO;

import java.util.List;
import java.util.UUID;

public interface PurchaseOrderService {

    List<SupplierPurchaseOrderDTO> getAll();

    List<SupplierPurchaseOrderDTO> getSupplierPurchaseOrders(UUID supplierId);

    boolean givePurchaseOffer(SupplierPurchaseOrderDTO dto);
}
