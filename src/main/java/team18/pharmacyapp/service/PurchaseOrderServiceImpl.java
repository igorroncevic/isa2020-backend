package team18.pharmacyapp.service;

import com.sun.jdi.InternalException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team18.pharmacyapp.model.PurchaseOrder;
import team18.pharmacyapp.model.dtos.NewPurchaseOrderDTO;
import team18.pharmacyapp.model.dtos.PharmacyAdminInfoDTO;
import team18.pharmacyapp.model.dtos.PurchaseOrderDTO;
import team18.pharmacyapp.model.dtos.PurchaseOrderMedicineDTO;
import team18.pharmacyapp.model.exceptions.BadTimeRangeException;
import team18.pharmacyapp.model.exceptions.EntityNotFoundException;
import team18.pharmacyapp.model.exceptions.FailedToSaveException;
import team18.pharmacyapp.model.medicine.PurchaseOrderMedicine;
import team18.pharmacyapp.repository.PurchaseOrderRepository;
import team18.pharmacyapp.service.interfaces.PurchaseOrderService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class PurchaseOrderServiceImpl implements PurchaseOrderService {

    private final PurchaseOrderRepository purchaseOrderRepository;

    @Autowired
    public PurchaseOrderServiceImpl(PurchaseOrderRepository purchaseOrderRepository) {
        this.purchaseOrderRepository = purchaseOrderRepository;
    }

    @Override
    public List<PurchaseOrderDTO> getPharmacyPurchaseOrders(UUID pharmacyId) {
        List<PurchaseOrder> purchaseOrders = purchaseOrderRepository.getPharmacyPurchaseOrders(pharmacyId);
        List<PurchaseOrderDTO> purchaseOrderDTOs = new ArrayList<>();
        for(PurchaseOrder purchaseOrder : purchaseOrders) {
            PharmacyAdminInfoDTO pharmacyAdminInfoDTO = new PharmacyAdminInfoDTO(purchaseOrder.getPharmacyAdmin().getId(),
                    purchaseOrder.getPharmacyAdmin().getName(), purchaseOrder.getPharmacyAdmin().getSurname(),
                    purchaseOrder.getPharmacyAdmin().getEmail(), purchaseOrder.getPharmacyAdmin().getPhoneNumber());
            List<PurchaseOrderMedicine> purchaseOrderMedicines = purchaseOrderRepository.getPurchaseOrderMedicines(purchaseOrder.getId());
            List<PurchaseOrderMedicineDTO> purchaseOrderMedicineDTOs = new ArrayList<>();
            for(PurchaseOrderMedicine purchaseOrderMedicine : purchaseOrderMedicines) {
                PurchaseOrderMedicineDTO purchaseOrderMedicineDTO = new PurchaseOrderMedicineDTO(purchaseOrderMedicine.getMedicine().getId(),
                        purchaseOrderMedicine.getMedicine().getName(), purchaseOrderMedicine.getQuantity());
                purchaseOrderMedicineDTOs.add(purchaseOrderMedicineDTO);
            }
            PurchaseOrderDTO purchaseOrderDTO = new PurchaseOrderDTO(purchaseOrder.getId(), pharmacyAdminInfoDTO,
                    purchaseOrder.getEndDate(), purchaseOrderMedicineDTOs);
            purchaseOrderDTOs.add(purchaseOrderDTO);
        }
        return purchaseOrderDTOs;
    }

    @Override
    public PurchaseOrderDTO addPurchaseOrder(NewPurchaseOrderDTO newPurchaseOrderDTO) throws FailedToSaveException {
        UUID orderId = UUID.randomUUID();
        int rowsChanged = purchaseOrderRepository.insertPurchaseOrder(orderId, newPurchaseOrderDTO.getEndDate(), newPurchaseOrderDTO.getPharmacyAdminId());
        if(rowsChanged != 1) {
            throw new FailedToSaveException("Could not save new purchase order.");
        }
        for(PurchaseOrderMedicineDTO purchaseOrderMedicineDTO : newPurchaseOrderDTO.getMedicines()) {
            rowsChanged = purchaseOrderRepository.insertPurchaseOrderMedicine(orderId,
                    purchaseOrderMedicineDTO.getMedicineId(), purchaseOrderMedicineDTO.getOrderQuantity());
            if(rowsChanged != 1) {
                throw new FailedToSaveException("Could not save new purchase order medicine.");
            }
        }

        PurchaseOrder purchaseOrder = purchaseOrderRepository.getPurchaseOrderById(orderId);

        PharmacyAdminInfoDTO pharmacyAdminInfoDTO = new PharmacyAdminInfoDTO(purchaseOrder.getPharmacyAdmin().getId(),
                purchaseOrder.getPharmacyAdmin().getName(), purchaseOrder.getPharmacyAdmin().getSurname(),
                purchaseOrder.getPharmacyAdmin().getEmail(), purchaseOrder.getPharmacyAdmin().getPhoneNumber());
        List<PurchaseOrderMedicine> purchaseOrderMedicines = purchaseOrder.getPurchaseOrderMedicines();
        List<PurchaseOrderMedicineDTO> purchaseOrderMedicineDTOs = new ArrayList<>();
        for(PurchaseOrderMedicine purchaseOrderMedicine : purchaseOrderMedicines) {
            PurchaseOrderMedicineDTO purchaseOrderMedicineDTO = new PurchaseOrderMedicineDTO(purchaseOrderMedicine.getMedicine().getId(),
                    purchaseOrderMedicine.getMedicine().getName(), purchaseOrderMedicine.getQuantity());
            purchaseOrderMedicineDTOs.add(purchaseOrderMedicineDTO);
        }
        PurchaseOrderDTO purchaseOrderDTO = new PurchaseOrderDTO(purchaseOrder.getId(), pharmacyAdminInfoDTO,
                purchaseOrder.getEndDate(), purchaseOrderMedicineDTOs);

        return purchaseOrderDTO;
    }

}
