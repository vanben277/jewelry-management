package com.example.jewelry_management.service.impl;

import com.example.jewelry_management.dto.request.CreateCategory;
import com.example.jewelry_management.dto.request.FilterCategory;
import com.example.jewelry_management.dto.request.UpdateCategory;
import com.example.jewelry_management.dto.response.CategoryResponse;
import com.example.jewelry_management.exception.BusinessException;
import com.example.jewelry_management.exception.ErrorCodeConstant;
import com.example.jewelry_management.exception.NotFoundException;
import com.example.jewelry_management.mapper.CategoryMapper;
import com.example.jewelry_management.mapper.MapToCategory;
import com.example.jewelry_management.model.Category;
import com.example.jewelry_management.repository.CategoryRepository;
import com.example.jewelry_management.repository.ProductRepository;
import com.example.jewelry_management.repository.specification.CategorySpecification;
import com.example.jewelry_management.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final MapToCategory mapToCategory;
    private final CategoryMapper categoryMapper;

    public Category validateCategoryId(Integer id) {
        Optional<Category> existedById = categoryRepository.findByIdAndIsDeletedFalse(id);
        if (existedById.isEmpty()) {
            throw new NotFoundException("Id không tồn tại trong hệ thống", ErrorCodeConstant.CATEGORY_NOT_FOUND_ID);
        }
        return existedById.get();
    }

    @Override
    public Page<CategoryResponse> getByFilter(FilterCategory filterCategory) {
        Specification<Category> specification = CategorySpecification.nameConstant(filterCategory.getName())
                .and(CategorySpecification.notDeleted());
        Pageable pageable = PageRequest.of(filterCategory.getPageNumber(), filterCategory.getPageSize(), Sort.by("name").descending());
        Page<Category> categoryPage = categoryRepository.findAll(specification, pageable);
        return categoryPage.map(categoryMapper::toResponse);
    }

    @Override
    @Transactional
    public CategoryResponse createCategory(CreateCategory dto) {
        Category existedCategoryName = categoryRepository.findByName(dto.getName());
        if (existedCategoryName != null) {
            throw new BusinessException("Tên thể loại đã tồn tại trong hệ thống.", ErrorCodeConstant.CATEGORY_NAME_ALREADY_EXISTS);
        }

        Category createCategory = new Category();
        mapToCategory.mapDtoToCategory(dto, createCategory);

        if (dto.getParentId() != null) {
            Category parent = validateCategoryId(dto.getParentId());
            if (Boolean.TRUE.equals(parent.getIsDeleted())) {
                throw new BusinessException("Danh mục cha đã bị xóa khỏi hệ thống", ErrorCodeConstant.CATEGORY_HAS_BEEN_REMOVED_FROM_THE_SYSTEM);
            }
            createCategory.setParent(parent);
        }

        createCategory.setIsDeleted(false);

        Category saved = categoryRepository.save(createCategory);
        return categoryMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public CategoryResponse updateCategory(Integer id, UpdateCategory dto) {
        Category updateCategory = validateCategoryId(id);

        if (Boolean.TRUE.equals(updateCategory.getIsDeleted())) {
            throw new BusinessException("Thể loại đã được xóa khỏi hệ thống", ErrorCodeConstant.CATEGORY_HAS_BEEN_REMOVED_FROM_THE_SYSTEM);
        }

        Category existedByName = categoryRepository.findByName(dto.getName());
        if (existedByName != null && !existedByName.getId().equals(id)) {
            throw new BusinessException("Tên thể loại đã tồn tại trong hệ thống", ErrorCodeConstant.CATEGORY_NAME_ALREADY_EXISTS);
        }

        if (dto.getParentId() != null) {
            Category parent = validateCategoryId(dto.getParentId());
            if (Boolean.TRUE.equals(parent.getIsDeleted())) {
                throw new BusinessException("Danh mục cha đã bị xóa khỏi hệ thống", ErrorCodeConstant.CATEGORY_HAS_BEEN_REMOVED_FROM_THE_SYSTEM);
            }
            if (id.equals(dto.getParentId())) {
                throw new BusinessException("Danh mục không thể là cha của chính nó", ErrorCodeConstant.CATEGORY_SELF_REFERENCE);
            }
            updateCategory.setParent(parent);
        } else {
            updateCategory.setParent(null);
        }

        mapToCategory.mapDtoToCategory(dto, updateCategory);

        Category saveCategory = categoryRepository.save(updateCategory);
        return categoryMapper.toResponse(saveCategory);
    }


    @Override
    @Transactional
    public CategoryResponse softDeleteCategory(Integer id) {
        Category category = validateCategoryId(id);

        Boolean hasDeleted = productRepository.existsByCategoryIdAndIsDeletedFalse(id);
        if (hasDeleted) {
            throw new BusinessException("Không thể xóa vì có sản phẩm đang sử dụng!", ErrorCodeConstant.CATEGORY_HAS_PRODUCT);
        }

        List<Category> children = categoryRepository.findByParentIdAndIsDeletedFalse(id);
        if (!children.isEmpty()) {
            throw new BusinessException("Không thể xóa vì danh mục cha có danh mục con", ErrorCodeConstant.CATEGORY_HAS_CHILDREN);
        }

        category.setIsDeleted(true);
        Category saved = categoryRepository.save(category);
        categoryMapper.toResponse(saved);
        return null;
    }

    @Override
    @Transactional
    public CategoryResponse restoreDeleted(Integer id) {
        Category restoreCategory = categoryRepository.findByIdAndIsDeletedTrue(id);
        log.error("deletedError: {}", restoreCategory);
        if (restoreCategory == null) {
            throw new NotFoundException("Không tìm thấy id cần khôi phục", ErrorCodeConstant.CATEGORY_NOT_FOUND_ID);
        }
        restoreCategory.setIsDeleted(false);
        Category savedCategory = categoryRepository.save(restoreCategory);
        return categoryMapper.toResponse(savedCategory);
    }

    @Override
    public List<String> getAllCategoryName() {
        return categoryRepository.findAllCategoryNames();
    }
}
