package com.example.jewelry_management.dto.request;

import com.example.jewelry_management.model.Category;
import org.springframework.stereotype.Component;

@Component
public class MapToCategory {
    public void mapDtoToCategory(BaseDtoCategory dto, Category category) {
        category.setName(dto.getName());
        category.setDescription(dto.getDescription());
    }
}
