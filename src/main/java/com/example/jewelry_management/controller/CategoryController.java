package com.example.jewelry_management.controller;


import com.example.jewelry_management.dto.ApiResponse;
import com.example.jewelry_management.dto.res.AllCategoryNameResponse;
import com.example.jewelry_management.dto.res.CategoryResponse;
import com.example.jewelry_management.dto.res.CategoryTreeResponse;
import com.example.jewelry_management.form.CreateCategoryForm;
import com.example.jewelry_management.form.FilterCategoryForm;
import com.example.jewelry_management.form.UpdateCategoryForm;
import com.example.jewelry_management.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/category")
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("filter")
    public ResponseEntity<ApiResponse> getByFilter(FilterCategoryForm filterCategory) {
        Page<CategoryResponse> categories = categoryService.getByFilter(filterCategory);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Thành công", categories));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse> createCategory(@Valid @ModelAttribute CreateCategoryForm dto) {
        CategoryResponse category = categoryService.createCategory(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("Thành công", category));
    }

    @PutMapping(value = "{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse> updateCategory(@PathVariable Integer id, @Valid @ModelAttribute UpdateCategoryForm dto) {
        CategoryResponse categoryResponse = categoryService.updateCategory(id, dto);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Thành công", categoryResponse));
    }

    @DeleteMapping("soft-delete")
    public ResponseEntity<ApiResponse> softDeleteCategory(@RequestBody List<Integer> ids) {
        categoryService.softDeleteCategory(ids);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Xóa danh sách sản phẩm thành công", null));
    }

    @PutMapping("restore-delete")
    public ResponseEntity<ApiResponse> restoreDeleted(@RequestBody List<Integer> ids) {
        categoryService.restoreDeleted(ids);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Khôi phục danh sách sản phẩm thành công", null));
    }

    @GetMapping("name")
    public ResponseEntity<ApiResponse> getAllCategoryName() {
        List<AllCategoryNameResponse> names = categoryService.getAllCategoryName();
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Thành công", names));
    }

    @GetMapping("name-not-parent")
    public ResponseEntity<ApiResponse> getAllChildCategoryNames() {
        List<AllCategoryNameResponse> names = categoryService.getAllChildCategoryNames();
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Thành công", names));
    }

    @DeleteMapping("hard-delete")
    public ResponseEntity<ApiResponse> hardDeleteCategory(@RequestBody List<Integer> ids) {
        categoryService.deleteCategoryByIds(ids);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Xóa cứng danh sách sản phẩm thành công", null));
    }

    @GetMapping("tree")
    public ResponseEntity<ApiResponse> getCategoryTree() {
        List<CategoryTreeResponse> tree = categoryService.getCategoryTree();
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Thành công", tree));
    }

    @GetMapping("{id}")
    public ResponseEntity<ApiResponse> getById(@PathVariable Integer id) {
        CategoryResponse response = categoryService.getById(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Thành công!", response));
    }

}
