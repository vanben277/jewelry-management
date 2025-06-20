package com.example.jewelry_management.service.impl;

import com.example.jewelry_management.dto.request.CreateProduct;
import com.example.jewelry_management.dto.request.FilterProduct;
import com.example.jewelry_management.dto.request.MapToProduct;
import com.example.jewelry_management.dto.request.UpdateProduct;
import com.example.jewelry_management.exception.BusinessException;
import com.example.jewelry_management.exception.ErrorCodeConstant;
import com.example.jewelry_management.exception.NotFoundException;
import com.example.jewelry_management.model.Product;
import com.example.jewelry_management.repository.ProductRepository;
import com.example.jewelry_management.repository.specification.ProductSpecification;
import com.example.jewelry_management.service.ProductService;
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
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final MapToProduct mapToProduct;

    public Product validateId(Integer id) {
        Optional<Product> optProduct = productRepository.findByIdAndIsDeletedFalse(id);
        if (optProduct.isEmpty()) {
            throw new NotFoundException("Không tìm thấy id: " + id + " trong hệ thống", ErrorCodeConstant.PRODUCT_NOT_FOUND_ID);
        }
        return optProduct.get();
    }

    @Override
    public Page<Product> getByFilter(FilterProduct filterProduct) {
        Specification<Product> specification = ProductSpecification.nameContains(filterProduct.getName())
                .and(ProductSpecification.fromPrice(filterProduct.getFromPrice()))
                .and(ProductSpecification.toPrice(filterProduct.getToPrice()))
                .and(ProductSpecification.fromQuantity(filterProduct.getFromQuantity()))
                .and(ProductSpecification.toQuantity(filterProduct.getToQuantity()))
                .and(ProductSpecification.fromDateOfEntry(filterProduct.getFromDateOfEntry()))
                .and(ProductSpecification.toDateOfEntry(filterProduct.getToDateOfEntry()))
                .and(ProductSpecification.imageEquals(filterProduct.getImage()))
                .and(ProductSpecification.hasStatus(filterProduct.getStatus()))
                .and(ProductSpecification.categoryIdEquals(filterProduct.getCategoryId()))
                .and(ProductSpecification.notDeleted());
        Pageable pageable = PageRequest.of(filterProduct.getPageNumber(), filterProduct.getPageSize(), Sort.by("price").descending());
        return productRepository.findAll(specification, pageable);
    }

    @Override
    @Transactional
    public Product createProduct(CreateProduct dto) {

        Product existedProductName = productRepository.findByName(dto.getName());
        if (existedProductName != null) {
            throw new BusinessException("Tên sản phẩm đã tồn tại trong hệ thống", ErrorCodeConstant.PRODUCT_NAME_ALREADY_EXISTS);
        }

        Product createProduct = new Product();
        mapToProduct.mapDtoToProduct(dto, createProduct);
        createProduct.setIsDeleted(false);

        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));

        createProduct.setCreateAt(now);
        createProduct.setUpdateAt(now);

        return productRepository.save(createProduct);
    }

    @Override
    @Transactional
    public Product updateProduct(Integer id, UpdateProduct dto) {

        Product updateProduct = validateId(id);

        if (Boolean.TRUE.equals(updateProduct.getIsDeleted())) {
            throw new BusinessException("Sản phẩm đã bị xóa khỏi hệ thống", ErrorCodeConstant.PRODUCT_HAS_BEEN_REMOVED_FROM_THE_SYSTEM);
        }

        Product existedProductName = productRepository.findByName(dto.getName());
        if (existedProductName != null && !existedProductName.getId().equals(id)) {
            throw new BusinessException("Tên sản phẩm đã tồn tại trong hệ thống", ErrorCodeConstant.PRODUCT_NAME_ALREADY_EXISTS);
        }

        mapToProduct.mapDtoToProduct(dto, updateProduct);

        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));

        updateProduct.setUpdateAt(now);

        return productRepository.save(updateProduct);
    }

    @Override
    @Transactional
    public Product deleteProduct(Integer id) {
        Product product = validateId(id);
        product.setIsDeleted(true);
        product.setUpdateAt(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
        productRepository.save(product);
        return null;
    }
}
