package com.example.jewelry_management.controller;


import com.example.jewelry_management.dto.ApiResponse;
import com.example.jewelry_management.dto.request.CreateOrderRequest;
import com.example.jewelry_management.dto.request.OrderListByFilterDto;
import com.example.jewelry_management.dto.request.UpdateOrderStatus;
import com.example.jewelry_management.dto.response.OrderResponse;
import com.example.jewelry_management.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("order")
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<ApiResponse> createOrder(@Valid @RequestBody CreateOrderRequest request) {
        OrderResponse order = orderService.createOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("Thành công", order));
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getOrderListByFilter(@Valid OrderListByFilterDto dto) {
        Page<OrderResponse> order = orderService.getOrderListByFilter(dto);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Thành công", order));
    }

    @GetMapping("{id}")
    public ResponseEntity<ApiResponse> getOrderById(@PathVariable Integer id) {
        OrderResponse order = orderService.getOrderById(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Thành công", order));
    }

    @PutMapping("{id}/status")
    public ResponseEntity<ApiResponse> updateStatus(@PathVariable Integer id, @Valid @RequestBody UpdateOrderStatus dto) {
        OrderResponse order = orderService.updateStatus(id, dto);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Thành công", order));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse> softOrderDeleted(@PathVariable Integer id) {
        OrderResponse order = orderService.softOrderDeleted(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Thành công", order));
    }

    @PutMapping("{id}/restore")
    public ResponseEntity<ApiResponse> restoreOrderDeleted(@PathVariable Integer id) {
        OrderResponse order = orderService.restoreOrderDeleted(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Thành công", order));
    }

}
