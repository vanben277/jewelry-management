package com.example.jewelry_management.mapper;

import com.example.jewelry_management.dto.res.OrderItemResponse;
import com.example.jewelry_management.model.OrderItem;
import com.example.jewelry_management.model.ProductImage;
import org.springframework.stereotype.Component;

@Component
public class OrderItemMapper {
    public OrderItemResponse toItemResponse(OrderItem orderItem) {

        String imageUrl = orderItem.getProduct().getImages() != null && !orderItem.getProduct().getImages().isEmpty()
                ? orderItem.getProduct().getImages().stream()
                .filter(ProductImage::getIsPrimary)
                .findFirst()
                .orElse(orderItem.getProduct().getImages().getFirst())
                .getImageUrl()
                : null;

        return new OrderItemResponse(
                orderItem.getId(),
                orderItem.getOrder().getId(),
                orderItem.getProduct().getId(),
                orderItem.getProductName(),
                orderItem.getPrice(),
                orderItem.getQuantity(),
                orderItem.getTotal(),
                orderItem.getSize(),
                orderItem.getCreateAt(),
                imageUrl
        );
    }
}
