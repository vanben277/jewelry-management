package com.example.jewelry_management.service;

import com.example.jewelry_management.dto.request.CreateProduct;
import com.example.jewelry_management.dto.request.FilterProduct;
import com.example.jewelry_management.dto.request.UpdateProduct;
import com.example.jewelry_management.model.Product;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;

public interface ProductService {
    Product createProduct(CreateProduct dto);

    Product updateProduct(Integer id, @Valid UpdateProduct dto);

    Product softDeleteProduct(Integer id);

    Page<Product> getByFilter(FilterProduct filterProduct);

    Product restoreDeleted(Integer id);
}
