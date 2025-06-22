package com.example.jewelry_management.service;

import com.example.jewelry_management.dto.request.CreateOrderRequest;
import com.example.jewelry_management.dto.response.OrderResponse;
import jakarta.validation.Valid;

public interface OrderService {
    OrderResponse createOrder(@Valid CreateOrderRequest request);
}
