package com.example.jewelry_management.service;

import com.example.jewelry_management.dto.res.AllCategoryNameResponse;
import com.example.jewelry_management.dto.res.CategoryResponse;
import com.example.jewelry_management.dto.res.CategoryTreeResponse;
import com.example.jewelry_management.form.CreateCategoryForm;
import com.example.jewelry_management.form.FilterCategoryForm;
import com.example.jewelry_management.form.UpdateCategoryForm;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CategoryService {
    CategoryResponse createCategory(@Valid CreateCategoryForm dto);

    CategoryResponse updateCategory(Integer id, @Valid UpdateCategoryForm dto);

    void softDeleteCategory(List<Integer> id);

    Page<CategoryResponse> getByFilter(FilterCategoryForm filterCategory);

    List<AllCategoryNameResponse> getAllCategoryName();

    void restoreDeleted(List<Integer> id);

    void deleteCategoryByIds(List<Integer> ids);

    List<CategoryTreeResponse> getCategoryTree();

    CategoryResponse getById(Integer id);
}
