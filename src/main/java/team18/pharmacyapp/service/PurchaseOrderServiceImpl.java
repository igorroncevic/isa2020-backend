package team18.pharmacyapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team18.pharmacyapp.model.PurchaseOrder;
import team18.pharmacyapp.model.SupplierPurchaseOrder;
import team18.pharmacyapp.model.dtos.PurchaseOrderMedicineDTO;
import team18.pharmacyapp.model.dtos.SupplierPurchaseOrderDTO;
import team18.pharmacyapp.model.medicine.PurchaseOrderMedicine;
import team18.pharmacyapp.model.medicine.SupplierMedicine;
import team18.pharmacyapp.repository.MedicineRepository;
import team18.pharmacyapp.repository.PurchaseOrderRepository;
import team18.pharmacyapp.repository.SupplierPurchaseOrderRepository;
import team18.pharmacyapp.service.interfaces.PurchaseOrderService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class PurchaseOrderServiceImpl implements PurchaseOrderService {
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final SupplierPurchaseOrderRepository supplierPurchaseOrderRepository;
    private final MedicineRepository medicineRepository;

    @Autowired
    public PurchaseOrderServiceImpl(PurchaseOrderRepository purchaseOrderRepository, SupplierPurchaseOrderRepository supplierPurchaseOrderRepository, MedicineRepository medicineRepository) {
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.supplierPurchaseOrderRepository = supplierPurchaseOrderRepository;
        this.medicineRepository = medicineRepository;
    }

    @Override
    public List<SupplierPurchaseOrderDTO> getAll() {
        List<SupplierPurchaseOrderDTO> ret=new ArrayList();
        for(PurchaseOrder p: purchaseOrderRepository.findAll()){
            List<PurchaseOrderMedicineDTO> list=new ArrayList<>();
            for(PurchaseOrderMedicine pom:purchaseOrderRepository.findByPurchaseOrderId(p.getId())){
                list.add(new PurchaseOrderMedicineDTO(pom.getMedicine().getName(),pom.getQuantity()));
            }
            ret.add(new SupplierPurchaseOrderDTO(p.getId(), null, list, p.getEndDate(), 0, null));
        }
        return ret;
    }

    @Override
    public List<SupplierPurchaseOrderDTO> getSupplierPurchaseOrders(UUID supplierId) {
        List<SupplierPurchaseOrderDTO> ret=new ArrayList();
        SupplierPurchaseOrderDTO dto=new SupplierPurchaseOrderDTO();
        for(SupplierPurchaseOrder spo:purchaseOrderRepository.findByPurchaseOrderIdAndSupplier(supplierId)){
            List<PurchaseOrderMedicineDTO> list=new ArrayList<>();
            dto.setPurchaseOrderStatus(spo.getPurchaseOrder().getStatus());
            dto.setPrice(spo.getPrice());
            dto.setDeliveryDate(spo.getDeliveryDate());
            for(PurchaseOrderMedicine pom:purchaseOrderRepository.findByPurchaseOrderId(spo.getPurchaseOrder().getId())){
                list.add(new PurchaseOrderMedicineDTO(pom.getMedicine().getName(),pom.getQuantity()));
            }
            dto.setMedicines(list);
            ret.add(dto);
        }
        return ret;
    }

    @Override
    public boolean givePurchaseOffer(SupplierPurchaseOrderDTO dto) {
        if(supplierHasAllMedicines(dto)){
            supplierPurchaseOrderRepository.addNewSupplierPurchaseOrder(dto.getDeliveryDate(), dto.getPrice(), dto.getPurchaseOrderId(), dto.getSupplierId());
            return true;
        }
        return false;
    }

    private boolean supplierHasMedicine(PurchaseOrderMedicineDTO dto, UUID supplierId){
        for(SupplierMedicine supplierMedicine: medicineRepository.findMedicinesBySupplierId(supplierId)){
            System.out.println(supplierMedicine.getMedicine().getName());
            System.out.println(supplierMedicine.getQuantity());
            if(supplierMedicine.getMedicine().getName().equals(dto.getName()) && supplierMedicine.getQuantity()>=dto.getQuantity()){
                return true;
            }
        }
        return false;
    }

    private boolean supplierHasAllMedicines(SupplierPurchaseOrderDTO dto){
        for(PurchaseOrderMedicineDTO medicineDTO:dto.getMedicines()){
            if(!supplierHasMedicine(medicineDTO,dto.getSupplierId())){
                return false;
            }
        }
        return true;
    }
}
