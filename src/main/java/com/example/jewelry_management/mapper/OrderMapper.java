package com.example.jewelry_management.mapper;


import com.example.jewelry_management.dto.res.AccountSummaryRes;
import com.example.jewelry_management.dto.res.OrderItemResponse;
import com.example.jewelry_management.dto.res.OrderResponse;
import com.example.jewelry_management.model.Account;
import com.example.jewelry_management.model.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderMapper {
    private final OrderItemMapper orderItemMapper;

    private AccountSummaryRes toAccountResponse(Account account) {
        return new AccountSummaryRes(
                account.getId(),
                account.getUserName(),
                account.getFirstName(),
                account.getLastName(),
                account.getRole(),
                account.getDateOfBirth(),
                account.getGender(),
                account.getAvatar(),
                account.getPhone(),
                account.getAddress(),
                account.getEmail(),
                account.getStatus()
        );
    }

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
                order.getCreateAt(),
                order.getUpdateAt(),
                itemResponses,
                toAccountResponse(order.getAccount())
        );
    }
}
