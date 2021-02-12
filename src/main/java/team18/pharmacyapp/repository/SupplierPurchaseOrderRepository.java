package team18.pharmacyapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import team18.pharmacyapp.model.SupplierPurchaseOrder;
import team18.pharmacyapp.model.dtos.SupplierPurchaseOrderDTO;


import java.util.Date;
import java.util.UUID;

public interface SupplierPurchaseOrderRepository extends JpaRepository<SupplierPurchaseOrder, UUID> {

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "INSERT INTO supplier_purchase_order(delivery_date, price, purchase_order_id, supplier_id) VALUES (:deliveryDate, :price, :purchaseOrderId, :supplierId)")
    int addNewSupplierPurchaseOrder(@Param("deliveryDate")Date deliveryDate, @Param("price") double price, @Param("purchaseOrderId") UUID purchaseOrderId, @Param("supplierId") UUID supplierId);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE supplier_purchase_order SET delivery_date=:deliveryDate, price=:price WHERE purchase_order_id=:purchaseOrderId AND supplier_id=:supplierId")
    int updatePurchaseOffer(@Param("deliveryDate")Date deliveryDate, @Param("price") double price, @Param("purchaseOrderId") UUID purchaseOrderId, @Param("supplierId") UUID supplierId);

    @Query("SELECT m FROM SupplierPurchaseOrder m WHERE m.purchaseOrder.id=:purchaseOrderId AND m.supplier.id =:supplierId")
    SupplierPurchaseOrder findSupplierPurchaseOrder(UUID purchaseOrderId, UUID supplierId);
}
