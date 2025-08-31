package com.example.jewelry_management.repository;

import com.example.jewelry_management.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {
    @Query("select distinct oi.product.id from OrderItem oi where oi.product.id in :productIds")
    List<Integer> findReferencedProductIds(@Param("productIds") List<Integer> productIds);

    @Query("SELECT COALESCE(SUM(oi.quantity), 0) FROM OrderItem oi WHERE oi.product.id = :productId")
    Long getSoldQuantityByProductId(@Param("productId") Integer productId);
}
