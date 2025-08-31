package com.example.jewelry_management.repository;

import com.example.jewelry_management.form.ProductImageForm;
import com.example.jewelry_management.model.Product;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Integer>, JpaSpecificationExecutor<Product> {

    Product findByName(@NotBlank(message = "Trường tên không được bỏ trống") String name);

    Optional<Product> findByIdAndIsDeletedFalse(Integer id);

    @Query("SELECT p.id, p.name, p.price, SUM(oi.quantity) AS totalQuantity " +
            "FROM OrderItem oi " +
            "JOIN oi.order o " +
            "JOIN oi.product p " +
            "WHERE o.status = 'DELIVERED' " +
            "GROUP BY p.id, p.name, p.price " +
            "ORDER BY SUM(oi.quantity) DESC")
    List<Object[]> findTopSellingProducts();

    @Query("SELECT new com.example.jewelry_management.form.ProductImageForm(pi.imageUrl, pi.isPrimary) " +
            "FROM ProductImage pi WHERE pi.product.id = :productId")
    List<ProductImageForm> findImagesByProductId(@Param("productId") Integer productId);

    Product findBySku(@NotBlank(message = "Mã sản phẩm không được bỏ trống") String sku);

    boolean existsByCategoryIdInAndIsDeletedFalse(List<Integer> ids);

    @Query("SELECT coalesce(sum(p.quantity), 0) from Product p")
    Integer countInventory();
}
