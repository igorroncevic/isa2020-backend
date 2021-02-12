package team18.pharmacyapp.service.interfaces;

import javassist.NotFoundException;
import team18.pharmacyapp.model.dtos.NewPurchaseOrderDTO;
import team18.pharmacyapp.model.dtos.PurchaseOrderDTO;
import team18.pharmacyapp.model.dtos.PurchaseOrderOfferDTO;
import team18.pharmacyapp.model.exceptions.ActionNotAllowedException;
import team18.pharmacyapp.model.exceptions.FailedToSaveException;

import java.util.List;
import java.util.UUID;

public interface PurchaseOrderService {

    List<PurchaseOrderDTO> getPharmacyPurchaseOrders(UUID pharmacyId);

    PurchaseOrderDTO addPurchaseOrder(NewPurchaseOrderDTO newPurchaseOrderDTO) throws FailedToSaveException;

    List<PurchaseOrderOfferDTO> getAllOffersForOrder(UUID id);

    void acceptOffer(UUID orderId, UUID supplierId, UUID phadmin) throws ActionNotAllowedException, FailedToSaveException;

    PurchaseOrderDTO updatePurchaseOrder(UUID orderId, NewPurchaseOrderDTO newPurchaseOrderDTO) throws FailedToSaveException, ActionNotAllowedException;
}
