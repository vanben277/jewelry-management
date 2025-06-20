package com.example.jewelry_management.service;

import com.example.jewelry_management.dto.request.CreateCategory;
import com.example.jewelry_management.dto.request.FilterCategory;
import com.example.jewelry_management.dto.request.UpdateCategory;
import com.example.jewelry_management.model.Category;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CategoryService {
    Category createCategory(@Valid CreateCategory dto);

    Category updateCategory(Integer id, @Valid UpdateCategory dto);

    Category deleteCategory(Integer id);

    Page<Category> getByFilter(FilterCategory filterCategory);

    List<String> getAllCategoryName();
}
