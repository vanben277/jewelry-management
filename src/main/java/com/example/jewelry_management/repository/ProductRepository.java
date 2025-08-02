package com.example.jewelry_management.repository;

import com.example.jewelry_management.form.ProductImageForm;
import com.example.jewelry_management.model.Product;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Integer>, JpaSpecificationExecutor<Product> {

    Product findByName(@NotBlank(message = "Trường tên không được bỏ trống") String name);

    Boolean existsByCategoryIdAndIsDeletedFalse(Integer id);

    Optional<Product> findByIdAndIsDeletedFalse(Integer id);

    Product findByIdAndIsDeletedTrue(Integer id);

    @Query("SELECT p.id, p.name, SUM(oi.quantity) AS totalQuantity, " +
            "SUM(oi.quantity * oi.price) AS totalRevenue " +
            "FROM OrderItem oi " +
            "JOIN oi.order o " +
            "JOIN oi.product p " +
            "WHERE o.status = 'DELIVERED' AND o.isDeleted = false " +
            "AND (:categoryId IS NULL OR p.category.id = :categoryId) " +
            "AND o.createAt BETWEEN :start AND :end " +
            "GROUP BY p.id, p.name " +
            "ORDER BY SUM(oi.quantity) DESC")
    List<Object[]> findTopSellingProducts(@Param("start") LocalDateTime start,
                                          @Param("end") LocalDateTime end,
                                          @Param("categoryId") Integer categoryId);

    @Query("SELECT new com.example.jewelry_management.form.ProductImageForm(pi.imageUrl, pi.isPrimary) " +
            "FROM ProductImage pi WHERE pi.product.id = :productId")
    List<ProductImageForm> findImagesByProductId(@Param("productId") Integer productId);

    Product findBySku(@NotBlank(message = "Mã sản phẩm không được bỏ trống") String sku);

    boolean existsByCategoryIdInAndIsDeletedFalse(List<Integer> ids);

    List<Product> findByCategoryIdAndIsDeletedFalse(Integer id);
}
