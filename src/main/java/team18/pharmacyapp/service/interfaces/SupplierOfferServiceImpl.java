package team18.pharmacyapp.service.interfaces;

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
import team18.pharmacyapp.repository.SupplierOfferRepository;
import team18.pharmacyapp.repository.SupplierPurchaseOrderRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class SupplierOfferServiceImpl implements SupplierOfferService{

    private final SupplierOfferRepository supplierOfferRepository;
    private final SupplierPurchaseOrderRepository supplierPurchaseOrderRepository;
    private final MedicineRepository medicineRepository;

    @Autowired
    public SupplierOfferServiceImpl(SupplierOfferRepository supplierOfferRepository, SupplierPurchaseOrderRepository supplierPurchaseOrderRepository, MedicineRepository medicineRepository) {
        this.supplierOfferRepository = supplierOfferRepository;
        this.supplierPurchaseOrderRepository = supplierPurchaseOrderRepository;
        this.medicineRepository = medicineRepository;
    }

    @Override
    public List<SupplierPurchaseOrderDTO> getAll() {
        List<SupplierPurchaseOrderDTO> ret=new ArrayList();
        for(PurchaseOrder p: supplierOfferRepository.findAll()){
            List<PurchaseOrderMedicineDTO> list=new ArrayList<>();
            for(PurchaseOrderMedicine pom:supplierOfferRepository.findByPurchaseOrderId(p.getId())){
                list.add(new PurchaseOrderMedicineDTO(pom.getMedicine().getName(),pom.getQuantity()));
            }
            ret.add(new SupplierPurchaseOrderDTO(p.getId(), null, list, p.getEndDate(), 0, null));
        }
        return ret;
    }

    @Override
    public List<SupplierPurchaseOrderDTO> getSupplierPurchaseOrders(UUID supplierId) {
        List<SupplierPurchaseOrderDTO> ret=new ArrayList();
        for(SupplierPurchaseOrder spo:supplierOfferRepository.findByPurchaseOrderIdAndSupplier(supplierId)){
            SupplierPurchaseOrderDTO dto=new SupplierPurchaseOrderDTO();
            List<PurchaseOrderMedicineDTO> list=new ArrayList<>();
            dto.setPurchaseOrderStatus(spo.getPurchaseOrder().getStatus());
            dto.setPrice(spo.getPrice());
            dto.setDeliveryDate(spo.getDeliveryDate());
            dto.setPurchaseOrderId(spo.getPurchaseOrder().getId());
            for(PurchaseOrderMedicine pom:supplierOfferRepository.findByPurchaseOrderId(spo.getPurchaseOrder().getId())){
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

    @Override
    public boolean update(SupplierPurchaseOrderDTO dto) {
        PurchaseOrder po=supplierOfferRepository.findById(dto.getPurchaseOrderId()).orElse(null);
        if(po==null){
            return false;
        }
        if(dto.getDeliveryDate().isAfter(po.getEndDate())){
            return false;
        }
        SupplierPurchaseOrder spo = findById(dto.getPurchaseOrderId(), dto.getSupplierId());
        if(spo != null){
            supplierPurchaseOrderRepository.updatePurchaseOffer(dto.getDeliveryDate(), dto.getPrice(), spo.getPurchaseOrder().getId(), spo.getSupplier().getId());
            return true;
        }

        return false;
    }

    @Override
    public SupplierPurchaseOrder findById(UUID purchaseOrderId, UUID supplierId) {
        return supplierPurchaseOrderRepository.findSupplierPurchaseOrder(purchaseOrderId, supplierId);
    }

    private boolean supplierHasMedicine(PurchaseOrderMedicineDTO dto, UUID supplierId){
        for(SupplierMedicine supplierMedicine: medicineRepository.findMedicinesBySupplierId(supplierId)){
            System.out.println(supplierMedicine.getMedicine().getName());
            System.out.println(supplierMedicine.getQuantity());
            if(supplierMedicine.getMedicine().getName().equals(dto.getMedicineName()) && supplierMedicine.getQuantity()>=dto.getOrderQuantity()){
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
