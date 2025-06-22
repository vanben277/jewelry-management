package com.example.jewelry_management.service.impl;

import com.example.jewelry_management.repository.OrderItemRepository;
import com.example.jewelry_management.service.OrderItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderItemServiceImpl implements OrderItemService {
    private final OrderItemRepository orderItemRepository;
}
