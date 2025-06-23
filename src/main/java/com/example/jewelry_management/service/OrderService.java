package com.example.jewelry_management.service;

import com.example.jewelry_management.dto.request.CreateOrderRequest;
import com.example.jewelry_management.dto.request.OrderListByFilterDto;
import com.example.jewelry_management.dto.request.UpdateOrderStatus;
import com.example.jewelry_management.dto.response.OrderResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;

public interface OrderService {
    OrderResponse createOrder(@Valid CreateOrderRequest request);

    Page<OrderResponse> getOrderListByFilter(@Valid OrderListByFilterDto dto);

    OrderResponse getOrderById(Integer id);

    OrderResponse updateStatus(Integer id, @Valid UpdateOrderStatus dto);

    OrderResponse softOrderDeleted(Integer id);

    OrderResponse restoreOrderDeleted(Integer id);
}
