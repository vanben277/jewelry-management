package com.example.jewelry_management.mapper;

import com.example.jewelry_management.dto.response.OrderItemResponse;
import com.example.jewelry_management.model.OrderItem;
import org.springframework.stereotype.Component;

@Component
public class OrderItemMapper {
    public OrderItemResponse toItemResponse(OrderItem orderItem) {
        return new OrderItemResponse(
                orderItem.getId(),
                orderItem.getOrderId().getId(),
                orderItem.getProductId().getId(),
                orderItem.getProductName(),
                orderItem.getPrice(),
                orderItem.getQuantity(),
                orderItem.getTotal(),
                orderItem.getCreateAt()
        );
    }
}
