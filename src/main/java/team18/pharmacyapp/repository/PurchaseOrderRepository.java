package team18.pharmacyapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import team18.pharmacyapp.model.PurchaseOrder;
import team18.pharmacyapp.model.SupplierPurchaseOrder;
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

    @Query("SELECT spo FROM supplier_purchase_order spo JOIN FETCH spo.purchaseOrder JOIN FETCH spo.supplier WHERE spo.purchaseOrder.id = :id ")
    List<SupplierPurchaseOrder> getAllOffersForOrder(UUID id);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE supplier_purchase_order SET accepted = true WHERE purchase_order_id=:orderId and supplier_id=:supplierId")
    int acceptOffer(UUID orderId, UUID supplierId);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE pharmacy_medicines SET quantity = quantity + :quantity WHERE pharmacy_id=:pharmacyId AND medicine_id=:medicineId")
    int addQuantityToPharmacyMedicine(UUID pharmacyId, UUID medicineId, int quantity);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE supplier_medicine SET quantity = quantity - :quantity WHERE supplier_id=:supplierId AND medicine_id=:medicineId")
    int subtractQuantityFromSupplierMedicine(UUID supplierId, UUID medicineId, int quantity);

    @Query(nativeQuery = true, value = "SELECT CAST(medicine_id as VARCHAR )medicine_id FROM pharmacy_medicines WHERE pharmacy_id = :pharmacyId")
    List<String> getPharmacyMedicineUUIDs(UUID pharmacyId);



}
