package com.example.jewelry_management.validator;

import com.example.jewelry_management.exception.ErrorCodeConstant;
import com.example.jewelry_management.exception.NotFoundException;
import com.example.jewelry_management.model.Category;
import com.example.jewelry_management.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CategoryValidator {
    private final CategoryRepository categoryRepository;

    public Category validateCategoryId(Integer id) {
        Optional<Category> existedById = categoryRepository.findByIdAndIsDeletedFalse(id);
        if (existedById.isEmpty()) {
            throw new NotFoundException("Id không tồn tại trong hệ thống", ErrorCodeConstant.CATEGORY_NOT_FOUND_ID);
        }
        return existedById.get();
    }
}
