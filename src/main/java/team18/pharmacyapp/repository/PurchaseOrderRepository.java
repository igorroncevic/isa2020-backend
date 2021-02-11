package team18.pharmacyapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import team18.pharmacyapp.model.PurchaseOrder;
import team18.pharmacyapp.model.Term;
import team18.pharmacyapp.model.medicine.PurchaseOrderMedicine;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, UUID> {

    @Query("SELECT po FROM purchase_order po JOIN FETCH po.pharmacyAdmin pha WHERE po.pharmacyAdmin.pharmacy.id = :pharmacyId ")
    List<PurchaseOrder> getPharmacyPurchaseOrders(@Param("pharmacyId") UUID pharmacyId);

    @Query("SELECT po FROM purchase_order po JOIN FETCH po.pharmacyAdmin pha JOIN FETCH po.purchaseOrderMedicines pom " +
            "JOIN FETCH pom.medicine WHERE po.id = :id ")
    PurchaseOrder getPurchaseOrderById(UUID id);

    @Query("SELECT pom FROM purchase_order_medicine pom JOIN FETCH pom.medicine m WHERE pom.purchaseOrder.id = :orderId ")
    List<PurchaseOrderMedicine> getPurchaseOrderMedicines(@Param("orderId") UUID orderId);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "INSERT INTO purchase_order(id, end_date, pharmacy_admin_id)" +
            "VALUES(:id, :endDate, :phadminId)")
    int insertPurchaseOrder(UUID id, Date endDate, UUID phadminId);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "INSERT INTO purchase_order_medicine(purchase_order_id, medicine_id, quantity)" +
            "VALUES(:orderId, :medicineId, :quantity)")
    int insertPurchaseOrderMedicine(UUID orderId, UUID medicineId, int quantity);

}
