package com.example.jewelry_management.service;

import com.example.jewelry_management.dto.res.OrderResponse;
import com.example.jewelry_management.dto.res.RevenueReportResponse;
import com.example.jewelry_management.enums.OrderStatus;
import com.example.jewelry_management.form.CreateOrderRequestForm;
import com.example.jewelry_management.form.OrderListByFilterForm;
import com.example.jewelry_management.form.RevenueFilterForm;
import com.example.jewelry_management.form.UpdateOrderStatusForm;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;

import java.util.List;

public interface OrderService {
    OrderResponse createOrder(@Valid CreateOrderRequestForm request);

    Page<OrderResponse> getOrderListByFilter(@Valid OrderListByFilterForm dto);

    OrderResponse getOrderById(Integer id);

    OrderResponse updateStatus(Integer id, @Valid UpdateOrderStatusForm dto);

    OrderResponse softOrderDeleted(Integer id);

    OrderResponse restoreOrderDeleted(Integer id);

    List<RevenueReportResponse> getRevenueReport(RevenueFilterForm filter);

    List<String> getAllOrderStatus();

    List<OrderResponse> getAllOrdersByMe(Integer id, OrderStatus status);
}
