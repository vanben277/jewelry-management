package com.example.jewelry_management.mapper;

import com.example.jewelry_management.dto.res.CategoryResponse;
import com.example.jewelry_management.model.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {
    public CategoryResponse toResponse(Category category) {
        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getDescription(),
                category.getCreateAt(),
                category.getUpdateAt(),
                category.getIsDeleted(),
                category.getParent() != null ? category.getParent().getId() : null,
                category.getBannerUrl()
        );
    }
}
