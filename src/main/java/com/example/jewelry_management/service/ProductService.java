package com.example.jewelry_management.service;

import com.example.jewelry_management.dto.res.ProductResponse;
import com.example.jewelry_management.dto.res.TopProductResponse;
import com.example.jewelry_management.form.*;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {
    ProductResponse createProduct(@Valid CreateProductForm dto, MultipartFile[] images);

    ProductResponse updateProduct(Integer id, @Valid UpdateProductForm dto, List<MultipartFile> imageFiles);

    void softDeleteMultiple(List<Integer> ids);

    Page<ProductResponse> getByFilter(FilterProductForm filterProduct);

    void restoreDeleteMultiple(List<Integer> id);

    List<TopProductResponse> getTopSellingProducts(@Valid TopProductFilterForm filter);

    List<String> getAllStatus();

    ProductResponse findById(Integer id);

    void deleteMultiple(List<Integer> ids);

    List<String> getAllGoldType();


    List<ProductResponse> latestProducts();

    Page<ProductResponse> getProductsByCategoryId(Integer id, FilterProductsByCategoryForm filter);
}
