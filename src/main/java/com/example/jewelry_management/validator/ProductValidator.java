package com.example.jewelry_management.validator;

import com.example.jewelry_management.exception.ErrorCodeConstant;
import com.example.jewelry_management.exception.NotFoundException;
import com.example.jewelry_management.model.Product;
import com.example.jewelry_management.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProductValidator {
    private final ProductRepository productRepository;

    public Product validateId(Integer id) {
        Optional<Product> optProduct = productRepository.findByIdAndIsDeletedFalse(id);
        if (optProduct.isEmpty()) {
            throw new NotFoundException("Không tìm thấy id: " + id + " trong hệ thống", ErrorCodeConstant.PRODUCT_NOT_FOUND_ID);
        }
        return optProduct.get();
    }
}
