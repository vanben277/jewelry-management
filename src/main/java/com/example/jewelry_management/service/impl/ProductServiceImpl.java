package com.example.jewelry_management.service.impl;

import com.example.jewelry_management.dto.res.ProductResponse;
import com.example.jewelry_management.dto.res.TopProductResponse;
import com.example.jewelry_management.enums.AccountRole;
import com.example.jewelry_management.enums.GoldType;
import com.example.jewelry_management.enums.ProductStatus;
import com.example.jewelry_management.exception.BusinessException;
import com.example.jewelry_management.exception.ErrorCodeConstant;
import com.example.jewelry_management.exception.NotFoundException;
import com.example.jewelry_management.exception.UnauthorizedException;
import com.example.jewelry_management.form.*;
import com.example.jewelry_management.mapper.MapToProduct;
import com.example.jewelry_management.mapper.ProductMapper;
import com.example.jewelry_management.model.*;
import com.example.jewelry_management.repository.CategoryRepository;
import com.example.jewelry_management.repository.OrderItemRepository;
import com.example.jewelry_management.repository.ProductRepository;
import com.example.jewelry_management.repository.specification.ProductSpecification;
import com.example.jewelry_management.service.FileStorageService;
import com.example.jewelry_management.service.ProductService;
import com.example.jewelry_management.utils.AccountValidatorUtils;
import com.example.jewelry_management.utils.ProductValidatorUtils;
import com.example.jewelry_management.validator.AccountValidator;
import com.example.jewelry_management.validator.ProductValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final MapToProduct mapToProduct;
    private final ProductMapper productMapper;
    private final OrderItemRepository orderItemRepository;
    private final FileStorageService fileStorageService;
    private final ProductValidatorUtils validatorUtils;
    private final ProductValidator productValidator;
    private final CategoryRepository categoryRepository;
    private final AccountValidatorUtils accountValidatorUtils;
    private final AccountValidator accountValidator;

    @Override
    public Page<ProductResponse> getByFilter(FilterProductForm filterProduct) {
        Account account = accountValidator.getCurrentAccount();

        if (!account.getRole().equals(AccountRole.ADMIN)) {
            throw new UnauthorizedException("Bạn không có quyền truy cập", ErrorCodeConstant.UNAUTHORIZED);
        }

        accountValidatorUtils.validatorAccountStatus(account);

        Specification<Product> specification = ProductSpecification.nameAndSkuCombinedSearch(filterProduct.getName())
                .and(ProductSpecification.categoryEquals(filterProduct.getCategoryId()))
                .and(ProductSpecification.hasStatus(filterProduct.getStatus()))
                .and(ProductSpecification.goldTypeEquals(filterProduct.getGoldType()))
                .and(ProductSpecification.isDeletedEquals(filterProduct.getIsDeleted()));
        Pageable pageable = PageRequest.of(filterProduct.getPageNumber(), filterProduct.getPageSize(), Sort.by("createAt").descending());
        Page<Product> saved = productRepository.findAll(specification, pageable);
        return saved.map(productMapper::toProductResponse);
    }


    @Override
    @Transactional
    public ProductResponse createProduct(CreateProductForm dto, MultipartFile[] files) {
        Product existedProductName = productRepository.findByName(dto.getName());
        if (existedProductName != null) {
            throw new BusinessException("Tên sản phẩm đã tồn tại trong hệ thống", ErrorCodeConstant.PRODUCT_NAME_ALREADY_EXISTS);
        }

        Product existedSku = productRepository.findBySku(dto.getSku());
        if (existedSku != null) {
            throw new BusinessException("Mã sản phẩm đã tồn tại trong hệ thống", ErrorCodeConstant.PRODUCT_SKU_ALREADY_EXISTS);
        }

        Product product = new Product();
        mapToProduct.mapDtoToProduct(dto, product);
        product.setStatus(ProductStatus.IN_STOCK);
        product.setIsDeleted(false);

        if (dto.getGoldType() != null) {
            validatorUtils.validateGoldType(dto.getGoldType().name());
        }

        List<ProductImage> productImages = processMultipartImages(files, product);
        product.setImages(productImages);

        List<ProductSize> sizes = processSizes(dto.getSizes(), product);
        product.setSizes(sizes);

        if (sizes.isEmpty() && dto.getQuantity() != null) {
            product.setQuantity(dto.getQuantity());
        }

        product.setGoldType(dto.getGoldType());
        product.setSku(dto.getSku());

        Product saved = productRepository.save(product);
        return productMapper.toProductResponse(saved);
    }

    private List<ProductImage> processMultipartImages(MultipartFile[] files, Product product) {
        if (files == null || files.length == 0) {
            throw new BusinessException("Sản phẩm phải có ít nhất 1 ảnh", ErrorCodeConstant.INVALID_INPUT);
        }

        List<ProductImage> productImages = new ArrayList<>();
        for (int i = 0; i < files.length; i++) {
            MultipartFile file = files[i];
            String imageUrl = fileStorageService.storeImage(file, "products");

            ProductImage image = new ProductImage();
            image.setImageUrl(imageUrl);
            image.setIsPrimary(i == 0);
            image.setProduct(product);
            productImages.add(image);
        }

        return productImages;
    }

    private List<ProductSize> processSizes(List<ProductSizeForm> sizeForms, Product product) {
        List<ProductSize> sizes = new ArrayList<>();

        if (sizeForms != null && !sizeForms.isEmpty()) {
            for (ProductSizeForm sizeForm : sizeForms) {
                ProductSize size = new ProductSize();
                size.setSize(sizeForm.getSize());
                size.setQuantity(sizeForm.getQuantity());
                size.setProduct(product);
                sizes.add(size);
            }
        }

        return sizes;
    }

    @Override
    @Transactional
    public ProductResponse updateProduct(Integer id, UpdateProductForm dto, List<MultipartFile> imageFiles) {
        Product updateProduct = productValidator.validateId(id);

        validatorUtils.validateGoldType(dto.getGoldType() != null ? dto.getGoldType().name() : null);

        if (Boolean.TRUE.equals(updateProduct.getIsDeleted())) {
            throw new BusinessException("Sản phẩm đã bị xóa khỏi hệ thống", ErrorCodeConstant.PRODUCT_HAS_BEEN_REMOVED_FROM_THE_SYSTEM);
        }


        Product existedProductName = productRepository.findByName(dto.getName());
        if (existedProductName != null && !existedProductName.getId().equals(id)) {
            throw new BusinessException("Tên sản phẩm đã tồn tại trong hệ thống", ErrorCodeConstant.PRODUCT_NAME_ALREADY_EXISTS);
        }

        mapToProduct.mapDtoToProduct(dto, updateProduct);

        validatorUtils.validatorImages(dto.getImages());

        List<ProductImage> existingImages = updateProduct.getImages();
        List<ProductImage> allImages = new ArrayList<>(existingImages);

        if (imageFiles != null && !imageFiles.isEmpty()) {
            for (MultipartFile file : imageFiles) {
                if (!file.isEmpty()) {
                    String imageUrl = fileStorageService.storeImage(file, "products");
                    ProductImage img = new ProductImage();
                    img.setImageUrl(imageUrl);
                    img.setIsPrimary(false);
                    img.setProduct(updateProduct);
                    allImages.add(img);
                }
            }
        }

        if (dto.getImages() != null && !dto.getImages().isEmpty()) {
            for (ProductImageForm form : dto.getImages()) {
                allImages.stream()
                        .filter(img -> img.getImageUrl().equals(form.getImageUrl()))
                        .findFirst().ifPresent(existingImage -> existingImage.setIsPrimary(Boolean.TRUE.equals(form.getIsPrimary())));
            }
        }

        long countPrimary = allImages.stream().filter(ProductImage::getIsPrimary).count();
        if (countPrimary > 1) {
            throw new BusinessException("Chỉ được phép có 1 ảnh chính duy nhất", ErrorCodeConstant.INVALID_INPUT);
        }
        if (countPrimary == 0 && !allImages.isEmpty()) {
            allImages.getFirst().setIsPrimary(true);
        }

        if (dto.getImages() != null) {
            List<String> keptUrls = dto.getImages().stream()
                    .map(ProductImageForm::getImageUrl)
                    .toList();

            for (ProductImage oldImg : existingImages) {
                if (!keptUrls.contains(oldImg.getImageUrl())) {
                    fileStorageService.deleteFileByUrl(oldImg.getImageUrl());
                }
            }
        }

        updateProduct.getImages().clear();
        updateProduct.getImages().addAll(allImages);

        if (dto.getSizes() != null) {
            Set<Integer> sizeValues = new HashSet<>();
            for (ProductSizeForm form : dto.getSizes()) {
                if (!sizeValues.add(form.getSize())) {
                    throw new BusinessException("Size sản phẩm bị trùng. Vui lòng chọn size khác", ErrorCodeConstant.INVALID_INPUT);
                }
            }
        }

        List<ProductSize> existingSizes = updateProduct.getSizes();
        List<ProductSize> newSizes = new ArrayList<>();
        if (dto.getSizes() != null && !dto.getSizes().isEmpty()) {
            for (ProductSizeForm form : dto.getSizes()) {
                Integer sizeValue = form.getSize();
                ProductSize existingSize = existingSizes.stream()
                        .filter(s -> s.getSize().equals(sizeValue))
                        .findFirst()
                        .orElse(null);
                if (existingSize != null) {
                    existingSize.setQuantity(form.getQuantity() != null ? form.getQuantity() : 0);
                    newSizes.add(existingSize);
                } else {
                    ProductSize size = new ProductSize();
                    size.setSize(sizeValue);
                    size.setQuantity(form.getQuantity() != null ? form.getQuantity() : 0);
                    size.setProduct(updateProduct);
                    newSizes.add(size);
                }
            }
        } else {
            updateProduct.setQuantity(dto.getQuantity() != null ? dto.getQuantity() : 0);
        }

        updateProduct.getSizes().clear();
        updateProduct.getSizes().addAll(newSizes);

        updateProduct.updateStatus();

        Product saved = productRepository.save(updateProduct);
        return productMapper.toProductResponse(saved);
    }


    @Override
    @Transactional
    public void softDeleteMultiple(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new BusinessException("Danh sách ID không được rỗng", ErrorCodeConstant.INVALID_INPUT);
        }

        List<Product> products = productRepository.findAllById(ids);

        List<Product> activeProduct = products.stream()
                .filter(product -> Boolean.FALSE.equals(product.getIsDeleted()))
                .toList();

        List<Integer> foundIds = activeProduct.stream()
                .map(Product::getId)
                .toList();

        List<Integer> missingIds = ids.stream()
                .filter(id -> !foundIds.contains(id))
                .toList();

        if (!missingIds.isEmpty()) {
            throw new NotFoundException("id cần xóa " + missingIds + " không tồn tại trong  hệ thống", ErrorCodeConstant.PRODUCT_NOT_FOUND_ID);
        }

        List<Integer> referencedIds = orderItemRepository.findReferencedProductIds(ids);
        if (!referencedIds.isEmpty()) {
            throw new BusinessException("Không thể xóa vì sản phẩm đang được sử dụng trong đơn hàng. ID " + referencedIds,
                    ErrorCodeConstant.PRODUCT_BEING_REFERENCED);
        }

        for (Product product : activeProduct) {
            product.setIsDeleted(true);
        }
        productRepository.saveAll(products);
        log.info("Đã soft delete {} sản phẩm: {}", products.size(), products.stream().map(Product::getId).toList());
    }

    @Override
    @Transactional
    public void restoreDeleteMultiple(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new BusinessException("Danh sách id không được để rỗng", ErrorCodeConstant.INVALID_INPUT);
        }
        List<Product> products = productRepository.findAllById(ids);

        List<Product> activeDeleted = products.stream()
                .filter(product -> Boolean.TRUE.equals(product.getIsDeleted()))
                .toList();

        List<Integer> foundIds = activeDeleted.stream()
                .map(Product::getId)
                .toList();

        List<Integer> missingIds = ids.stream()
                .filter(id -> !foundIds.contains(id))
                .toList();

        if (!missingIds.isEmpty()) {
            throw new NotFoundException("id cần khôi phục " + missingIds + " không tồn tại trong  hệ thống", ErrorCodeConstant.PRODUCT_NOT_FOUND_ID);
        }

        for (Product product : activeDeleted) {
            product.setIsDeleted(false);
        }

        productRepository.saveAll(products);
    }

    @Override
    public List<TopProductResponse> getTopSellingProducts(TopProductFilterForm filter) {
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
            Integer productId = (Integer) result[0];
            dto.setProductId(productId);
            dto.setName((String) result[1]);
            dto.setTotalQuantitySold((Long) result[2]);
            dto.setTotalRevenue((BigDecimal) result[3]);
            List<ProductImageForm> images = productRepository.findImagesByProductId(productId);
            dto.setImages(images);
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public List<String> getAllStatus() {
        return Arrays.stream(ProductStatus.values())
                .map(Enum::name)
                .collect(Collectors.toList());
    }

    @Override
    public ProductResponse findById(Integer id) {
        Product product = productValidator.validateId(id);
        return productMapper.toProductResponse(product);
    }

    @Override
    @Transactional
    public void deleteMultiple(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new BusinessException("Danh sách cần xóa không được để trống", ErrorCodeConstant.INVALID_INPUT);
        }

        List<Product> products = productRepository.findAllById(ids);

        List<Integer> foundIds = products.stream()
                .map(Product::getId)
                .toList();

        List<Integer> missingIds = ids.stream()
                .filter(id -> !foundIds.contains(id))
                .toList();
        if (!missingIds.isEmpty()) {
            throw new NotFoundException("Id cần xóa " + missingIds + " không tìm thấy trong hệ thống", ErrorCodeConstant.PRODUCT_NOT_FOUND_ID);
        }

        List<Integer> referencedIds = orderItemRepository.findReferencedProductIds(ids);

        if (!referencedIds.isEmpty()) {
            throw new BusinessException("Không thể xóa vì sản phẩm đang được sử dụng trong đơn hàng. ID " + referencedIds,
                    ErrorCodeConstant.PRODUCT_BEING_REFERENCED);
        }

        productRepository.deleteAll(products);
    }

    @Override
    public List<String> getAllGoldType() {
        return Arrays.stream(GoldType.values())
                .map(Enum::name)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductResponse> latestProducts() {
        return productRepository.findAll(Sort.by(Sort.Direction.DESC, "dateOfEntry"))
                .stream()
                .limit(10)
                .map(productMapper::toProductResponse)
                .toList();
    }

    @Override
    public Page<ProductResponse> getProductsByCategoryId(Integer id, FilterProductsByCategoryForm filter) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Id sản phẩm không tồn tại", ErrorCodeConstant.CATEGORY_NOT_FOUND_ID));

        if (Boolean.TRUE.equals(category.getIsDeleted())) {
            throw new BusinessException("Sản phẩm đã được xóa khỏi hệ thống", ErrorCodeConstant.CATEGORY_HAS_BEEN_REMOVED_FROM_THE_SYSTEM);
        }

        long childCount = categoryRepository.countChildrenByParentId(id);
        if (childCount > 0) {
            throw new BusinessException("Không thể lấy danh sách vì đây là danh mục cha", ErrorCodeConstant.CATEGORY_IS_PARENT);
        }

        if (filter.getGoldType() != null) {
            try {
                GoldType.valueOf(String.valueOf(filter.getGoldType()));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Loại vàng không hợp lệ: '" + filter.getGoldType() + "'. Vui lòng chọn: GOLD_10K, GOLD_14K, GOLD_18K, GOLD_24K");
            }
        }

        Specification<Product> specification = ProductSpecification.nameAndSkuCombinedSearch(filter.getName())
                .and(ProductSpecification.categoryEquals(id))
                .and(ProductSpecification.goldTypeEquals(filter.getGoldType()))
                .and(ProductSpecification.fromPrice(filter.getFromPrice()))
                .and(ProductSpecification.toPrice(filter.getToPrice()))
                .and(ProductSpecification.notDeleted());

        Sort.Direction direction = filter.getSortDirection().equalsIgnoreCase("asc")
                ? Sort.Direction.ASC : Sort.Direction.DESC;
        List<String> allowedSortFields = List.of("price", "dateOfEntry");
        if (!allowedSortFields.contains(filter.getSortBy())) {
            throw new BusinessException("Trường sắp xếp không hợp lệ! Chỉ được: " + allowedSortFields, ErrorCodeConstant.INVALID_INPUT);
        }
        Sort sort = Sort.by(direction, filter.getSortBy());

        Pageable pageable = PageRequest.of(filter.getPageNumber(), filter.getPageSize(), sort);
        Page<Product> saved = productRepository.findAll(specification, pageable);
        return saved.map(productMapper::toProductResponse);
    }

    @Override
    public Page<ProductResponse> searchProducts(String name) {
        if (name == null || name.trim().isEmpty()) {
            return Page.empty();
        }
        Specification<Product> specification = ProductSpecification.nameAndSkuCombinedSearch(name)
                .and(ProductSpecification.notDeleted());
        Pageable pageable = PageRequest.of(0, 5);
        Page<Product> page = productRepository.findAll(specification, pageable);
        return page.map(productMapper::toProductResponse);
    }

    @Override
    @Transactional
    public void updatedStatusByInStockOrSoldOut(Integer id, UpdateProductStatusForm form) {
        Account account = accountValidator.getCurrentAccount();
        accountValidatorUtils.validatorAccountStatus(account);

        if (!EnumSet.of(ProductStatus.IN_STOCK, ProductStatus.SOLD_OUT).contains(form.getStatus())) {
            throw new BusinessException("Trạng thái sản phẩm không hợp lệ. Vui lòng truyền IN_STOCK, SOLD_OUT", ErrorCodeConstant.INVALID_INPUT);
        }

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy sản phẩm trong hệ thống", ErrorCodeConstant.PRODUCT_NOT_FOUND_ID));

        product.setStatus(form.getStatus());
        productRepository.save(product);
    }
}
