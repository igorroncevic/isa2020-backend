package team18.pharmacyapp.service.interfaces;

import team18.pharmacyapp.model.dtos.NewPurchaseOrderDTO;
import team18.pharmacyapp.model.dtos.PurchaseOrderDTO;
import team18.pharmacyapp.model.exceptions.FailedToSaveException;

import java.util.List;
import java.util.UUID;

public interface PurchaseOrderService {

    List<PurchaseOrderDTO> getPharmacyPurchaseOrders(UUID pharmacyId);

    PurchaseOrderDTO addPurchaseOrder(NewPurchaseOrderDTO newPurchaseOrderDTO) throws FailedToSaveException;
}
