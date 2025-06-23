package com.example.jewelry_management.service.impl;

import com.example.jewelry_management.dto.request.CreateProduct;
import com.example.jewelry_management.dto.request.FilterProduct;
import com.example.jewelry_management.dto.request.TopProductFilterDto;
import com.example.jewelry_management.dto.request.UpdateProduct;
import com.example.jewelry_management.dto.response.ProductResponse;
import com.example.jewelry_management.dto.response.TopProductResponse;
import com.example.jewelry_management.exception.BusinessException;
import com.example.jewelry_management.exception.ErrorCodeConstant;
import com.example.jewelry_management.exception.NotFoundException;
import com.example.jewelry_management.mapper.MapToProduct;
import com.example.jewelry_management.mapper.ProductMapper;
import com.example.jewelry_management.model.Product;
import com.example.jewelry_management.model.ProductStatus;
import com.example.jewelry_management.repository.ProductRepository;
import com.example.jewelry_management.repository.specification.ProductSpecification;
import com.example.jewelry_management.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final MapToProduct mapToProduct;
    private final ProductMapper productMapper;

    public Product validateId(Integer id) {
        Optional<Product> optProduct = productRepository.findByIdAndIsDeletedFalse(id);
        if (optProduct.isEmpty()) {
            throw new NotFoundException("Không tìm thấy id: " + id + " trong hệ thống", ErrorCodeConstant.PRODUCT_NOT_FOUND_ID);
        }
        return optProduct.get();
    }

    @Override
    public Page<ProductResponse> getByFilter(FilterProduct filterProduct) {
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
        Page<Product> saved = productRepository.findAll(specification, pageable);
        return saved.map(productMapper::toProductResponse);
    }

    @Override
    @Transactional
    public ProductResponse createProduct(CreateProduct dto) {

        Product existedProductName = productRepository.findByName(dto.getName());
        if (existedProductName != null) {
            throw new BusinessException("Tên sản phẩm đã tồn tại trong hệ thống", ErrorCodeConstant.PRODUCT_NAME_ALREADY_EXISTS);
        }

        Product createProduct = new Product();
        mapToProduct.mapDtoToProduct(dto, createProduct);
        createProduct.setStatus(ProductStatus.IN_STOCK);
        createProduct.setIsDeleted(false);
        Product saved = productRepository.save(createProduct);
        return productMapper.toProductResponse(saved);
    }

    @Override
    @Transactional
    public ProductResponse updateProduct(Integer id, UpdateProduct dto) {

        Product updateProduct = validateId(id);

        if (Boolean.TRUE.equals(updateProduct.getIsDeleted())) {
            throw new BusinessException("Sản phẩm đã bị xóa khỏi hệ thống", ErrorCodeConstant.PRODUCT_HAS_BEEN_REMOVED_FROM_THE_SYSTEM);
        }

        Product existedProductName = productRepository.findByName(dto.getName());
        if (existedProductName != null && !existedProductName.getId().equals(id)) {
            throw new BusinessException("Tên sản phẩm đã tồn tại trong hệ thống", ErrorCodeConstant.PRODUCT_NAME_ALREADY_EXISTS);
        }

        mapToProduct.mapDtoToProduct(dto, updateProduct);
        updateProduct.setStatus(dto.getStatus());

        Product saved = productRepository.save(updateProduct);
        return productMapper.toProductResponse(saved);
    }

    @Override
    @Transactional
    public ProductResponse softDeleteProduct(Integer id) {
        Product product = validateId(id);
        product.setIsDeleted(true);
        Product saved = productRepository.save(product);
        productMapper.toProductResponse(saved);
        return null;
    }

    @Override
    @Transactional
    public ProductResponse restoreDeleted(Integer id) {
        Product restoreProduct = productRepository.findByIdAndIsDeletedTrue(id);
        if (restoreProduct == null) {
            throw new NotFoundException("Không tìm thấy sản phẩm cần khôi phục", ErrorCodeConstant.PRODUCT_NOT_FOUND_ID);
        }
        restoreProduct.setIsDeleted(false);
        Product saved = productRepository.save(restoreProduct);
        return productMapper.toProductResponse(saved);
    }

    @Override
    public List<TopProductResponse> getTopSellingProducts(TopProductFilterDto filter) {
        if (filter == null || filter.getTopN() == null) {
            log.error("Top không được bỏ trống");
            throw new IllegalArgumentException("Top không được bỏ trống");
        }

        LocalDateTime start = filter.getStartDate() != null ? LocalDateTime.parse(filter.getStartDate() + "T00:00:00") : LocalDateTime.now().minusMonths(1);
        LocalDateTime end = filter.getEndDate() != null ? LocalDateTime.parse(filter.getEndDate() + "T00:00:00") : LocalDateTime.now();

        log.debug("Đang lấy các sản phẩm bán chạy nhất {} từ {} đến {} với categoryId: {}",
                filter.getTopN(), start, end, filter.getCategoryId());

        List<Object[]> results = productRepository.findTopSellingProducts(start, end, filter.getCategoryId());
        return results.stream().limit(filter.getTopN()).map(result -> {
            TopProductResponse dto = new TopProductResponse();
            dto.setProductId((Integer) result[0]);
            dto.setName((String) result[1]);
            dto.setTotalQuantitySold((Long) result[2]);
            dto.setTotalRevenue((BigDecimal) result[3]);
            return dto;
        }).collect(Collectors.toList());
    }
}
