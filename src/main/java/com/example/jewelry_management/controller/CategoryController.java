package com.example.jewelry_management.controller;


import com.example.jewelry_management.dto.ApiResponse;
import com.example.jewelry_management.dto.request.CreateCategory;
import com.example.jewelry_management.dto.request.FilterCategory;
import com.example.jewelry_management.dto.request.UpdateCategory;
import com.example.jewelry_management.model.Category;
import com.example.jewelry_management.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("category")
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<ApiResponse> getByFilter(FilterCategory filterCategory) {
        Page<Category> categories = categoryService.getByFilter(filterCategory);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Thành công", categories));
    }

    @PostMapping
    public ResponseEntity<ApiResponse> createCategory(@Valid @RequestBody CreateCategory dto) {
        Category category = categoryService.createCategory(dto);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Thành công", category));
    }

    @PutMapping("{id}")
    public ResponseEntity<ApiResponse> updateCategory(@PathVariable Integer id, @Valid @RequestBody UpdateCategory dto) {
        Category category = categoryService.updateCategory(id, dto);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Thành công", category));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse> deleteCategory(@PathVariable Integer id) {
        Category category = categoryService.deleteCategory(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Thành công", category));
    }

    @GetMapping("name")
    public ResponseEntity<ApiResponse> getAllCategoryName() {
        List<String> names = categoryService.getAllCategoryName();
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Thành công", names));
    }
}
