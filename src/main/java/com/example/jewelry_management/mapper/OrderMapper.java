package com.example.jewelry_management.mapper;


import com.example.jewelry_management.dto.response.OrderItemResponse;
import com.example.jewelry_management.dto.response.OrderResponse;
import com.example.jewelry_management.model.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderMapper {
    private final OrderItemMapper orderItemMapper;

    public OrderResponse toResponse(Order order) {
        List<OrderItemResponse> itemResponses = order.getItems().stream()
                .map(orderItemMapper::toItemResponse)
                .toList();
        return new OrderResponse(
                order.getId(),
                order.getCustomerName(),
                order.getCustomerPhone(),
                order.getCustomerAddress(),
                order.getStatus(),
                order.getTotalPrice(),
                order.getIsDeleted(),
                order.getCreateAt(),
                order.getUpdateAt(),
                itemResponses
        );
    }
}
