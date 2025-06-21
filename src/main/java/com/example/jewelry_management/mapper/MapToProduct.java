package com.example.jewelry_management.mapper;

import com.example.jewelry_management.dto.base.BaseDtoProduct;
import com.example.jewelry_management.model.Product;
import org.springframework.stereotype.Component;

@Component
public class MapToProduct {
    public void mapDtoToProduct(BaseDtoProduct dto, Product product) {
        product.setName(dto.getName());
        product.setPrice(dto.getPrice());
        product.setQuantity(dto.getQuantity());
        product.setDateOfEntry(dto.getDateOfEntry());
        product.setImage(dto.getImage());
        product.setDescription(dto.getDescription());
        product.setStatus(dto.getStatus());
        product.setCategoryId(dto.getCategoryId());
    }
}
