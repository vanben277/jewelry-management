package com.example.jewelry_management.repository;

import com.example.jewelry_management.form.ProductSizeForm;
import com.example.jewelry_management.model.ProductSize;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductSizeRepository extends JpaRepository<ProductSize, Integer> {
    Optional<ProductSize> findByProductIdAndSize(Integer productId, Integer size);

    boolean existsByProductId(Integer productId);
}
