package com.example.jewelry_management.controller;


import com.example.jewelry_management.dto.ApiResponse;
import com.example.jewelry_management.dto.request.CreateProduct;
import com.example.jewelry_management.dto.request.FilterProduct;
import com.example.jewelry_management.dto.request.UpdateProduct;
import com.example.jewelry_management.model.Product;
import com.example.jewelry_management.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("product")
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public ResponseEntity<ApiResponse> getByFilter(FilterProduct filterProduct) {
        Page<Product> products = productService.getByFilter(filterProduct);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Thành công", products));
    }

    @PostMapping
    public ResponseEntity<ApiResponse> createProduct(@Valid @RequestBody CreateProduct dto) {
        Product product = productService.createProduct(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("Thành công", product));
    }

    @PutMapping("{id}")
    public ResponseEntity<ApiResponse> updateProduct(@PathVariable Integer id, @Valid @RequestBody UpdateProduct dto) {
        Product product = productService.updateProduct(id, dto);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Thành công", product));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse> softDeleteProduct(@PathVariable Integer id) {
        Product product = productService.softDeleteProduct(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Thành công", product));
    }

    @PutMapping("restore/{id}")
    public ResponseEntity<ApiResponse> restoreDeleted(@PathVariable Integer id) {
        Product product = productService.restoreDeleted(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Thành công", product));
    }
}
