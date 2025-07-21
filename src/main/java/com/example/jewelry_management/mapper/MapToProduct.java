package com.example.jewelry_management.mapper;

import com.example.jewelry_management.dto.base.BaseDtoProduct;
import com.example.jewelry_management.model.Category;
import com.example.jewelry_management.model.Product;
import org.springframework.stereotype.Component;

@Component
public class MapToProduct {
    public void mapDtoToProduct(BaseDtoProduct dto, Product product) {
        product.setName(dto.getName());
        product.setPrice(dto.getPrice());
        product.setQuantity(dto.getQuantity());
        product.setDateOfEntry(dto.getDateOfEntry());
        product.setDescription(dto.getDescription());
        if (dto.getCategoryId() != null) {
            Category category = new Category();
            category.setId(dto.getCategoryId());
            product.setCategory(category);
        } else {
            product.setCategory(null);
        }
        product.setGoldType(dto.getGoldType());
    }
}
