package team18.pharmacyapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import team18.pharmacyapp.model.PurchaseOrder;
import team18.pharmacyapp.model.SupplierPurchaseOrder;
import team18.pharmacyapp.model.medicine.PurchaseOrderMedicine;

import java.util.List;
import java.util.UUID;

public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, UUID> {

    @Query("select pom from PurchaseOrderMedicine pom join fetch pom.medicine m where pom.purchaseOrder.id=:purchaseOrderId")
    List<PurchaseOrderMedicine> findByPurchaseOrderId(UUID purchaseOrderId);

    @Query("select s from SupplierPurchaseOrder s join fetch s.purchaseOrder where s.supplier.id=:supplierId")
    List<SupplierPurchaseOrder> findByPurchaseOrderIdAndSupplier(UUID supplierId);

}
