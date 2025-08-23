package com.example.jewelry_management.service.impl;

import com.example.jewelry_management.dto.res.AllCategoryNameResponse;
import com.example.jewelry_management.dto.res.CategoryResponse;
import com.example.jewelry_management.dto.res.CategoryTreeResponse;
import com.example.jewelry_management.exception.BusinessException;
import com.example.jewelry_management.exception.ErrorCodeConstant;
import com.example.jewelry_management.exception.NotFoundException;
import com.example.jewelry_management.form.CreateCategoryForm;
import com.example.jewelry_management.form.FilterCategoryForm;
import com.example.jewelry_management.form.UpdateCategoryForm;
import com.example.jewelry_management.mapper.CategoryMapper;
import com.example.jewelry_management.mapper.MapToCategory;
import com.example.jewelry_management.model.Category;
import com.example.jewelry_management.repository.CategoryRepository;
import com.example.jewelry_management.repository.ProductRepository;
import com.example.jewelry_management.repository.specification.CategorySpecification;
import com.example.jewelry_management.service.CategoryService;
import com.example.jewelry_management.service.FileStorageService;
import com.example.jewelry_management.validator.CategoryValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final MapToCategory mapToCategory;
    private final CategoryMapper categoryMapper;
    private final ModelMapper modelMapper;
    private final FileStorageService fileStorageService;
    private final CategoryValidator validator;

    @Override
    public Page<CategoryResponse> getByFilter(FilterCategoryForm filterCategory) {
        Specification<Category> specification = CategorySpecification.nameConstant(filterCategory.getName());
        Pageable pageable = PageRequest.of(filterCategory.getPageNumber(), filterCategory.getPageSize(), Sort.by("createAt").descending());
        Page<Category> categoryPage = categoryRepository.findAll(specification, pageable);
        return categoryPage.map(categoryMapper::toResponse);
    }

    @Override
    @Transactional
    public CategoryResponse createCategory(CreateCategoryForm dto) {
        Category existedCategoryName = categoryRepository.findByName(dto.getName());
        if (existedCategoryName != null) {
            throw new BusinessException("Tên thể loại đã tồn tại trong hệ thống.", ErrorCodeConstant.CATEGORY_NAME_ALREADY_EXISTS);
        }

        Category createCategory = new Category();
        mapToCategory.mapDtoToCategory(dto, createCategory);

        if (dto.getParentId() != null) {
            Category parent = validator.validateCategoryId(dto.getParentId());
            if (Boolean.TRUE.equals(parent.getIsDeleted())) {
                throw new BusinessException("Danh mục cha đã bị xóa khỏi hệ thống", ErrorCodeConstant.CATEGORY_HAS_BEEN_REMOVED_FROM_THE_SYSTEM);
            }
            createCategory.setParent(parent);
        }

        if (dto.getBannerUrl() != null && !dto.getBannerUrl().isEmpty()) {
            String imageUrl = fileStorageService.storeImage(dto.getBannerUrl(), "categories");
            createCategory.setBannerUrl(imageUrl);
        }

        createCategory.setIsDeleted(false);

        Category saved = categoryRepository.save(createCategory);
        return categoryMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public CategoryResponse updateCategory(Integer id, UpdateCategoryForm dto) {
        Category updateCategory = validator.validateCategoryId(id);

        if (Boolean.TRUE.equals(updateCategory.getIsDeleted())) {
            throw new BusinessException("Thể loại đã được xóa khỏi hệ thống", ErrorCodeConstant.CATEGORY_HAS_BEEN_REMOVED_FROM_THE_SYSTEM);
        }

        Category existedByName = categoryRepository.findByName(dto.getName());
        if (existedByName != null && !existedByName.getId().equals(id)) {
            throw new BusinessException("Tên thể loại đã tồn tại trong hệ thống", ErrorCodeConstant.CATEGORY_NAME_ALREADY_EXISTS);
        }

        if (dto.getParentId() != null) {
            Category parent = validator.validateCategoryId(dto.getParentId());
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

        MultipartFile newBannerFile = dto.getBannerUrl();
        if (newBannerFile != null && !newBannerFile.isEmpty()) {
            String newBannerUrl = fileStorageService.storeImage(newBannerFile, "categories");

            if (updateCategory.getBannerUrl() != null) {
                fileStorageService.deleteFileByUrl(updateCategory.getBannerUrl());
            }

            updateCategory.setBannerUrl(newBannerUrl);
        }

        mapToCategory.mapDtoToCategory(dto, updateCategory);

        Category saveCategory = categoryRepository.save(updateCategory);
        return categoryMapper.toResponse(saveCategory);
    }


    @Override
    @Transactional
    public void softDeleteCategory(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new BusinessException("Danh sách danh mục cần khôi phục không được để trống", ErrorCodeConstant.INVALID_INPUT);
        }

        List<Category> categories = categoryRepository.findAllByIdInAndIsDeletedFalse(ids);
        List<Integer> foundIds = categories.stream().map(Category::getId).toList();

        List<Integer> missingIds = ids.stream()
                .filter(id -> !foundIds.contains(id))
                .toList();

        if (!missingIds.isEmpty()) {
            throw new NotFoundException("Không tìm thấy danh mục cần xóa với ID: " + missingIds, ErrorCodeConstant.CATEGORY_NOT_FOUND_ID);
        }

        List<Category> children = categoryRepository.findByParentIdInAndIsDeletedFalse(ids);
        if (!children.isEmpty()) {
            throw new BusinessException("Không thể xoá vì một trong các danh mục là cha của danh mục khác", ErrorCodeConstant.CATEGORY_HAS_CHILDREN);
        }

        for (Category category : categories) {
            category.setIsDeleted(true);
        }

        categoryRepository.saveAll(categories);
        log.info("Đã xoá mềm các danh mục: {}", ids);
    }

    @Override
    @Transactional
    public void restoreDeleted(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new BusinessException("Danh sách danh mục cần khôi phục không được để trống", ErrorCodeConstant.INVALID_INPUT);
        }

        List<Category> categories = categoryRepository.findAllByIdInAndIsDeletedTrue(ids);
        List<Integer> foundIds = categories.stream().map(Category::getId).toList();

        List<Integer> missingIds = ids.stream()
                .filter(id -> !foundIds.contains(id))
                .toList();

        if (!missingIds.isEmpty()) {
            throw new NotFoundException("Không tìm thấy danh mục cần khôi phục với ID: " + missingIds, ErrorCodeConstant.CATEGORY_NOT_FOUND_ID);
        }

        boolean hasActiveProduct = productRepository.existsByCategoryIdInAndIsDeletedFalse(ids);
        if (hasActiveProduct) {
            throw new BusinessException("Không thể khôi phục danh mục vì đang có sản phẩm hoạt động liên kết với nó",
                    ErrorCodeConstant.CATEGORY_HAS_PRODUCT);
        }

        for (Category category : categories) {
            boolean nameConflict = categoryRepository.existsByNameAndIsDeletedFalse(category.getName());
            if (nameConflict) {
                throw new BusinessException("Không thể khôi phục sản phẩm '" + category.getName() + "' vì tên đã tồn tại", ErrorCodeConstant.PRODUCT_NAME_ALREADY_EXISTS);
            }
            category.setIsDeleted(false);
        }

        categoryRepository.saveAll(categories);
        log.info("Khôi phục thành công các danh mục: {}", ids);
    }


    @Override
    public List<AllCategoryNameResponse> getAllCategoryName() {
        return categoryRepository.findAllCategoryNames();
    }

    @Override
    @Transactional
    public void deleteCategoryByIds(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new BusinessException("Danh sách danh mục cần xóa không được để trống", ErrorCodeConstant.INVALID_INPUT);
        }

        List<Category> categories = categoryRepository.findAllById(ids);
        List<Integer> foundIds = categories.stream().map(Category::getId).toList();
        List<Integer> missingIds = ids.stream().filter(id -> !foundIds.contains(id)).toList();
        if (!missingIds.isEmpty()) {
            throw new NotFoundException("Danh mục cần xóa không tìm thấy với ID: " + missingIds, ErrorCodeConstant.CATEGORY_NOT_FOUND_ID);
        }


        List<Category> children = categoryRepository.findByParentIdInAndIsDeletedFalse(ids);
        if (!children.isEmpty()) {
            throw new BusinessException("Không thể xóa vì một trong các danh mục là cha của danh mục khác", ErrorCodeConstant.CATEGORY_HAS_CHILDREN);
        }

        categoryRepository.deleteAll(categories);
        log.info("Đã xóa cứng các danh mục: {}", ids);
    }

    @Override
    public List<CategoryTreeResponse> getCategoryTree() {
        List<Category> topLevelCategories = categoryRepository.findByParentIsNullAndIsDeletedFalse();
        return topLevelCategories.stream()
                .map(this::buildCategoryTree)
                .collect(Collectors.toList());
    }

    private CategoryTreeResponse buildCategoryTree(Category category) {
        CategoryTreeResponse response = modelMapper.map(category, CategoryTreeResponse.class);
        List<Category> children = categoryRepository.findByParentIdAndIsDeletedFalse(category.getId());
        if (!children.isEmpty()) {
            response.setChildren(children.stream()
                    .map(this::buildCategoryTree)
                    .collect(Collectors.toList()));
        }
        return response;
    }

    @Override
    public CategoryResponse getById(Integer id) {
        Category category = validator.validateCategoryId(id);
        return modelMapper.map(category, CategoryResponse.class);
    }

    @Override
    public List<AllCategoryNameResponse> getAllChildCategoryNames() {
        return categoryRepository.findAllChildCategoryNames();
    }
}
