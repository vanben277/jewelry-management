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
        return OrderResponse.builder()
                .id(order.getId())
                .customerName(order.getCustomerName())
                .customerPhone(order.getCustomerPhone())
                .customerAddress(order.getCustomerAddress())
                .status(order.getStatus())
                .paymentMethod(order.getPaymentMethod())
                .totalPrice(order.getTotalPrice())
                .createAt(order.getCreateAt())
                .updateAt(order.getUpdateAt())
                .items(itemResponses)
                .accountResponse(toAccountResponse(order.getAccount()))
                .qrCodeUrl(order.getQrCodeUrl())  // ← Map qrCodeUrl from entity
                .build();
    }
}
