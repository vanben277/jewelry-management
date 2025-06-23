package com.example.jewelry_management.service;

import com.example.jewelry_management.dto.request.CreateProduct;
import com.example.jewelry_management.dto.request.FilterProduct;
import com.example.jewelry_management.dto.request.TopProductFilterDto;
import com.example.jewelry_management.dto.request.UpdateProduct;
import com.example.jewelry_management.dto.response.ProductResponse;
import com.example.jewelry_management.dto.response.TopProductResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {
    ProductResponse createProduct(CreateProduct dto);

    ProductResponse updateProduct(Integer id, @Valid UpdateProduct dto);

    ProductResponse softDeleteProduct(Integer id);

    Page<ProductResponse> getByFilter(FilterProduct filterProduct);

    ProductResponse restoreDeleted(Integer id);

    List<TopProductResponse> getTopSellingProducts(TopProductFilterDto filter);
}
