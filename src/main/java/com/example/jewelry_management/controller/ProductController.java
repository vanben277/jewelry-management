package com.example.jewelry_management.controller;


import com.example.jewelry_management.dto.ApiResponse;
import com.example.jewelry_management.dto.res.ProductResponse;
import com.example.jewelry_management.dto.res.TopProductResponse;
import com.example.jewelry_management.form.*;
import com.example.jewelry_management.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/product")
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public ResponseEntity<ApiResponse> getByFilter(FilterProductForm filterProduct) {
        Page<ProductResponse> products = productService.getByFilter(filterProduct);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Thành công", products));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse> createProduct(@Valid CreateProductForm dto,
                                                     @RequestPart("images") MultipartFile[] images) {
        ProductResponse product = productService.createProduct(dto, images);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("Thành công", product));
    }

    @PutMapping("{id}")
    public ResponseEntity<ApiResponse> updateProduct(@PathVariable Integer id,
                                                     @Valid @ModelAttribute UpdateProductForm dto,
                                                     @RequestPart(value = "imageFiles", required = false) List<MultipartFile> imageFiles) {
        ProductResponse product = productService.updateProduct(id, dto, imageFiles);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Thành công", product));
    }

    @DeleteMapping("soft-deleted")
    public ResponseEntity<ApiResponse> softDeleteMultiple(@RequestBody List<Integer> ids) {
        productService.softDeleteMultiple(ids);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Cập nhật xóa mềm thành công", null));
    }

    @PutMapping("restore-deleted")
    public ResponseEntity<ApiResponse> restoreDeleteMultiple(@RequestBody List<Integer> ids) {
        productService.restoreDeleteMultiple(ids);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Khôi phục sản phẩm thành công", null));
    }

    @GetMapping("top-selling")
    public ResponseEntity<ApiResponse> getTopSellingProducts(@Valid @ModelAttribute TopProductFilterForm filter) {
        List<TopProductResponse> products = productService.getTopSellingProducts(filter);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Lấy danh sách sản phẩm bán chạy thành công", products));
    }

    @GetMapping("status")
    public ResponseEntity<ApiResponse> getAllStatus() {
        List<String> status = productService.getAllStatus();
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Thành công", status));
    }

    @GetMapping("{id}")
    public ResponseEntity<ApiResponse> findById(@PathVariable Integer id) {
        ProductResponse product = productService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Thành công", product));
    }

    @DeleteMapping("delete-multiple")
    public ResponseEntity<ApiResponse> deleteMultiple(@RequestBody List<Integer> ids) {
        productService.deleteMultiple(ids);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Khôi phục sản phẩm thành công", null));
    }

    @GetMapping("gold-type")
    public ResponseEntity<ApiResponse> getAllGoldType() {
        List<String> goldType = productService.getAllGoldType();
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Thành công", goldType));
    }

    @GetMapping("latest")
    public ResponseEntity<ApiResponse> latestProducts() {
        List<ProductResponse> productResponses = productService.latestProducts();
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Thành công", productResponses));

    }

    @GetMapping("category/{id}")
    public ResponseEntity<ApiResponse> getProductsByCategoryId (@PathVariable Integer id, FilterProductsByCategoryForm filter) {
        Page<ProductResponse> productResponses = productService.getProductsByCategoryId(id, filter);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Thành công", productResponses));
    }
}
