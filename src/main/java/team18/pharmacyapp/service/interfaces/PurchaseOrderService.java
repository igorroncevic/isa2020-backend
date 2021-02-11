package team18.pharmacyapp.service.interfaces;

import team18.pharmacyapp.model.dtos.PurchaseOrderDTO;

import java.util.List;
import java.util.UUID;

public interface PurchaseOrderService {

    List<PurchaseOrderDTO> getPharmacyPurchaseOrders(UUID pharmacyId);

}
