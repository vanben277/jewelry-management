package com.example.jewelry_management.controller;

import com.example.jewelry_management.service.OrderItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("order_item")
public class OrderItemController {
    private final OrderItemService orderItemService;
}
