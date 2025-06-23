package com.example.jewelry_management.mapper;

import com.example.jewelry_management.dto.response.ProductResponse;
import com.example.jewelry_management.model.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {
    public ProductResponse toProductResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getQuantity(),
                product.getDateOfEntry(),
                product.getImage(),
                product.getDescription(),
                product.getStatus(),
                product.getCreateAt(),
                product.getUpdateAt(),
                product.getCategoryId(),
                product.getIsDeleted()
        );
    }
}
