package com.example.jewelry_management.utils;

import com.example.jewelry_management.enums.OrderStatus;
import com.example.jewelry_management.exception.BusinessException;
import com.example.jewelry_management.exception.ErrorCodeConstant;
import com.example.jewelry_management.exception.NotFoundException;
import com.example.jewelry_management.model.Product;
import com.example.jewelry_management.model.ProductSize;
import com.example.jewelry_management.repository.ProductRepository;
import com.example.jewelry_management.repository.ProductSizeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderValidatorUtils {
    private final ProductRepository productRepository;
    private final ProductSizeRepository productSizeRepository;

    @Transactional
    public void validateAndReduceStock(Integer productId, Integer size, Integer quantity) {
        if (quantity <= 0) {
            throw new BusinessException("Số lượng phải lớn hơn 0", ErrorCodeConstant.INVALID_QUANTITY);
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException(
                        "Không tìm thấy sản phẩm với id: " + productId,
                        ErrorCodeConstant.PRODUCT_NOT_FOUND_ID));

        boolean hasSize = productSizeRepository.existsByProductId(productId);

        if (hasSize) {
            handleProductWithSize(product, size, quantity);
        } else {
            if (size != null) {
                throw new BusinessException(
                        "Sản phẩm " + product.getName() + " không có size, không được phép chọn size",
                        ErrorCodeConstant.PRODUCT_SIZE_NOT_ALLOWED
                );
            }
            handleProductWithoutSize(product, quantity);
        }
    }

    private void handleProductWithSize(Product product, Integer size, Integer quantity) {
        if (size == null) {
            throw new BusinessException(
                    "Sản phẩm " + product.getName() + " yêu cầu chọn size",
                    ErrorCodeConstant.PRODUCT_SIZE_REQUEST
            );
        }
        ProductSize productSize = productSizeRepository.findByProductIdAndSize(product.getId(), size)
                .orElseThrow(() -> new NotFoundException(
                        "Size " + size + " không tồn tại cho sản phẩm " + product.getName(),
                        ErrorCodeConstant.PRODUCT_SIZE_NOT_FOUND
                ));

        if (productSize.getQuantity() < quantity) {
            throw new BusinessException(
                    "Sản phẩm " + product.getName() + " size " + size + " chỉ còn "
                            + productSize.getQuantity() + " sản phẩm, không đủ " + quantity + " sản phẩm yêu cầu",
                    ErrorCodeConstant.OUT_OF_STOCK
            );
        }

        if (product.getQuantity() < quantity) {
            throw new BusinessException(
                    "Sản phẩm " + product.getName() + " chỉ còn " + product.getQuantity()
                            + " sản phẩm, không đủ " + quantity + " sản phẩm yêu cầu",
                    ErrorCodeConstant.OUT_OF_STOCK
            );
        }

        productSize.setQuantity(productSize.getQuantity() - quantity);
        product.setQuantity(product.getQuantity() - quantity);

        // Save changes
        productSizeRepository.save(productSize);
        productRepository.save(product);
    }

    private void handleProductWithoutSize(Product product, Integer quantity) {
        // Check stock availability
        if (product.getQuantity() < quantity) {
            throw new BusinessException(
                    "Sản phẩm " + product.getName() + " chỉ còn " + product.getQuantity()
                            + " sản phẩm, không đủ " + quantity + " sản phẩm yêu cầu",
                    ErrorCodeConstant.OUT_OF_STOCK
            );
        }

        product.setQuantity(product.getQuantity() - quantity);
        productRepository.save(product);
    }

    //     logic(Không được chuyển ngược)
    //     Từ PENDING -> CONFIRMED or CANCELLED
    //     Từ CONFIRMED -> SHIPPED or CANCELLED.
    //     Từ SHIPPED -> DELIVERED.
    public void validateStatusTransactional(OrderStatus currentStatus, OrderStatus newStatus) {
        switch (currentStatus) {
            case PENDING -> {
                if (newStatus != OrderStatus.CONFIRMED && newStatus != OrderStatus.CANCELLED)
                    throw new BusinessException("Không thể chuyển trạng thái từ PENDING sang " + newStatus, ErrorCodeConstant.INVALID_STATUS_TRANSACTIONAL);
            }
            case CONFIRMED -> {
                if (newStatus != OrderStatus.SHIPPED && newStatus != OrderStatus.CANCELLED)
                    throw new BusinessException("Không thể chuyển trạng thái từ CONFIRMED sang " + newStatus, ErrorCodeConstant.INVALID_STATUS_TRANSACTIONAL);
            }
            case SHIPPED -> {
                if (newStatus != OrderStatus.DELIVERED)
                    throw new BusinessException("Không thể chuyển trạng thái từ SHIPPED sang " + newStatus, ErrorCodeConstant.INVALID_STATUS_TRANSACTIONAL);
            }
            case DELIVERED, CANCELLED ->
                    throw new BusinessException("Không thể chuyển trạng thái từ sang " + currentStatus, ErrorCodeConstant.FINAL_STATUS);
            default ->
                    throw new BusinessException("Trạng thái hiện tại không hợp lệ " + newStatus, ErrorCodeConstant.INVALID_STATUS);
        }
    }
}
