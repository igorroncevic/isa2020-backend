package team18.pharmacyapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import team18.pharmacyapp.model.PurchaseOrder;
import team18.pharmacyapp.model.Term;
import team18.pharmacyapp.model.medicine.PurchaseOrderMedicine;

import java.util.List;
import java.util.UUID;

public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, UUID> {

    @Query("SELECT po FROM purchase_order po JOIN FETCH po.pharmacyAdmin pha WHERE po.pharmacyAdmin.pharmacy.id = :pharmacyId ")
    List<PurchaseOrder> getPharmacyPurchaseOrders(@Param("pharmacyId") UUID pharmacyId);

    @Query("SELECT pom FROM purchase_order_medicine pom JOIN FETCH pom.medicine m WHERE pom.purchaseOrder.id = :orderId ")
    List<PurchaseOrderMedicine> getPurchaseOrderMedicines(@Param("orderId") UUID orderId);


}
