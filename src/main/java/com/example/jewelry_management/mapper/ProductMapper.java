package com.example.jewelry_management.mapper;

import com.example.jewelry_management.dto.res.ProductResponse;
import com.example.jewelry_management.form.ProductImageForm;
import com.example.jewelry_management.form.ProductSizeForm;
import com.example.jewelry_management.model.Category;
import com.example.jewelry_management.model.Product;
import com.example.jewelry_management.model.ProductImage;
import com.example.jewelry_management.repository.OrderItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProductMapper {
    private final OrderItemRepository orderItemRepository;

    public ProductResponse toProductResponse(Product product) {
        ProductResponse response = new ProductResponse();
        response.setId(product.getId());
        response.setName(product.getName());
        response.setPrice(product.getPrice());
        response.setQuantity(product.getQuantity());
        response.setDateOfEntry(product.getDateOfEntry());
        response.setDescription(product.getDescription());
        response.setStatus(product.getStatus());
        response.setCreateAt(product.getCreateAt());
        response.setUpdateAt(product.getUpdateAt());

        Category category = product.getCategory();
        response.setCategoryId(category != null ? category.getId() : null);

        response.setIsDeleted(product.getIsDeleted());

        response.setPrimaryImageUrl(product.getImages()
                .stream()
                .filter(ProductImage::getIsPrimary)
                .map(ProductImage::getImageUrl)
                .findFirst()
                .orElse(null));

        List<ProductImageForm> imageForms = product.getImages().stream()
                .map(productImage -> {
                    ProductImageForm form = new ProductImageForm();
                    form.setImageUrl(productImage.getImageUrl());
                    form.setIsPrimary(productImage.getIsPrimary());
                    return form;
                })
                .collect(Collectors.toList());
        response.setImages(imageForms);

        List<ProductSizeForm> sizeForms = product.getSizes().stream()
                .map(productSize -> {
                    ProductSizeForm form = new ProductSizeForm();
                    form.setSize(productSize.getSize());
                    form.setQuantity(productSize.getQuantity());
                    return form;
                })
                .collect(Collectors.toList());
        response.setSizes(sizeForms);

        response.setSku(product.getSku());
        response.setGoldType(product.getGoldType());

        if (product.getCategory() != null) {
            response.setCategoryName(product.getCategory().getName());
        }

        Long soldQuantity = orderItemRepository.getSoldQuantityByProductId(product.getId());
        response.setSoldQuantity(soldQuantity);

        return response;
    }
}
