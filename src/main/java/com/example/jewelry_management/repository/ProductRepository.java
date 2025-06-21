package com.example.jewelry_management.repository;

import com.example.jewelry_management.model.Product;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Integer>, JpaSpecificationExecutor<Product> {

    Product findByName(@NotBlank(message = "Trường tên không được bỏ trống") String name);
    Boolean existsByCategoryIdAndIsDeletedFalse(Integer id);

    Optional<Product> findByIdAndIsDeletedFalse(Integer id);
}
