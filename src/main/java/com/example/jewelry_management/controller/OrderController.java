package com.example.jewelry_management.controller;


import com.example.jewelry_management.dto.ApiResponse;
import com.example.jewelry_management.dto.res.MonthRevenueListResponse;
import com.example.jewelry_management.dto.res.MonthlyRevenueResponse;
import com.example.jewelry_management.dto.res.OrderResponse;
import com.example.jewelry_management.dto.res.RevenueReportResponse;
import com.example.jewelry_management.enums.OrderStatus;
import com.example.jewelry_management.form.CreateOrderRequestForm;
import com.example.jewelry_management.form.OrderListByFilterForm;
import com.example.jewelry_management.form.RevenueFilterForm;
import com.example.jewelry_management.form.UpdateOrderStatusForm;
import com.example.jewelry_management.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/order")
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<ApiResponse> createOrder(@Valid @RequestBody CreateOrderRequestForm request) {
        OrderResponse order = orderService.createOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("Thành công", order));
    }

    @GetMapping("filter")
    public ResponseEntity<ApiResponse> getOrderListByFilter(@Valid OrderListByFilterForm dto) {
        Page<OrderResponse> order = orderService.getOrderListByFilter(dto);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Thành công", order));
    }

    @GetMapping("{id}")
    public ResponseEntity<ApiResponse> getOrderById(@PathVariable Integer id) {
        OrderResponse order = orderService.getOrderById(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Thành công", order));
    }

    @PutMapping("status/{id}")
    public ResponseEntity<ApiResponse> updateStatus(@PathVariable Integer id, @Valid @RequestBody UpdateOrderStatusForm dto) {
        OrderResponse order = orderService.updateStatus(id, dto);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Thành công", order));
    }

    @GetMapping("reports/revenue")
    public ResponseEntity<ApiResponse> getRevenueReport(@Valid @ModelAttribute RevenueFilterForm filter) {
        List<RevenueReportResponse> report = orderService.getRevenueReport(filter);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Thành công", report));
    }

    @GetMapping("status")
    public ResponseEntity<ApiResponse> getAllOrderStatus() {
        List<String> status = orderService.getAllOrderStatus();
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Thành công", status));
    }

    @GetMapping("me/{id}")
    public ResponseEntity<ApiResponse> getAllOrdersByMe(@PathVariable Integer id, OrderStatus status) {
        List<OrderResponse> orderResponses = orderService.getAllOrdersByMe(id, status);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Thành công", orderResponses));
    }

    @GetMapping("monthly-revenue")
    public ResponseEntity<ApiResponse> monthlyRevenue() {
        MonthlyRevenueResponse monthlyRevenue = orderService.monthlyRevenue();
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Thành công", monthlyRevenue));
    }

    @GetMapping("monthly")
    public ResponseEntity<ApiResponse> getMonthlyRevenue(
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false, defaultValue = "true") boolean millions) {

        List<MonthRevenueListResponse> data = orderService.getMonthlyRevenue(year, millions);
        return ResponseEntity.ok(new ApiResponse("Thành công", data));
    }
}
