package com.example.jewelry_management.service.impl;

import com.example.jewelry_management.dto.request.CreateCategory;
import com.example.jewelry_management.dto.request.FilterCategory;
import com.example.jewelry_management.dto.request.MapToCategory;
import com.example.jewelry_management.dto.request.UpdateCategory;
import com.example.jewelry_management.exception.BusinessException;
import com.example.jewelry_management.exception.ErrorCodeConstant;
import com.example.jewelry_management.exception.NotFoundException;
import com.example.jewelry_management.model.Category;
import com.example.jewelry_management.repository.CategoryRepository;
import com.example.jewelry_management.repository.ProductRepository;
import com.example.jewelry_management.repository.specification.CategorySpecification;
import com.example.jewelry_management.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final MapToCategory mapToCategory;

    public Category validateCategoryId(Integer id) {
        Optional<Category> existedById = categoryRepository.findByIdAndIsDeletedFalse(id);
        if (existedById.isEmpty()) {
            throw new NotFoundException("Id không tồn tại trong hệ thống", ErrorCodeConstant.CATEGORY_NOT_FOUND_ID);
        }
        return existedById.get();
    }

    @Override
    public Page<Category> getByFilter(FilterCategory filterCategory) {
        Specification<Category> specification = CategorySpecification.nameConstant(filterCategory.getName())
                .and(CategorySpecification.notDeleted());
        Pageable pageable = PageRequest.of(filterCategory.getPageNumber(), filterCategory.getPageSize(), Sort.by("name").descending());
        return categoryRepository.findAll(specification, pageable);
    }

    @Override
    @Transactional
    public Category createCategory(CreateCategory dto) {
        Category existedCategoryName = categoryRepository.findByName(dto.getName());
        if (existedCategoryName != null) {
            throw new BusinessException("Tên thể loại đã tồn tại trong hệ thống.", ErrorCodeConstant.CATEGORY_NAME_ALREADY_EXISTS);
        }

        Category createCategory = new Category();
        mapToCategory.mapDtoToCategory(dto, createCategory);

        createCategory.setIsDeleted(false);

        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
        createCategory.setCreateAt(now);
        createCategory.setUpdateAt(now);

        return categoryRepository.save(createCategory);
    }

    @Override
    @Transactional
    public Category updateCategory(Integer id, UpdateCategory dto) {
        Category updateCategory = validateCategoryId(id);

        if (Boolean.TRUE.equals(updateCategory.getIsDeleted())) {
            throw new BusinessException("Thể loại đã được xóa khỏi hệ thống", ErrorCodeConstant.CATEGORY_HAS_BEEN_REMOVED_FROM_THE_SYSTEM);
        }

        Category existedByName = categoryRepository.findByName(dto.getName());
        if (existedByName != null && !existedByName.getId().equals(id)) {
            throw new BusinessException("Tên thể loại đã tồn tại trong hệ thống", ErrorCodeConstant.CATEGORY_NAME_ALREADY_EXISTS);
        }

        mapToCategory.mapDtoToCategory(dto, updateCategory);

        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
        updateCategory.setUpdateAt(now);

        return categoryRepository.save(updateCategory);
    }


    @Override
    @Transactional
    public Category deleteCategory(Integer id) {
        Category category = validateCategoryId(id);

        Boolean hasDeleted = productRepository.existsByCategoryIdAndIsDeletedFalse(id);
        if (hasDeleted) {
            throw new BusinessException("Không thể xóa vì có sản phẩm đang sử dụng!", ErrorCodeConstant.CATEGORY_HAS_PRODUCT);
        }

        category.setIsDeleted(true);
        category.setUpdateAt(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
        categoryRepository.save(category);

        return null;
    }

    @Override
    public List<String> getAllCategoryName() {
        return categoryRepository.findAllCategoryNames();
    }
}
