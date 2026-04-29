package com.example.jewelry_management.dto.res;

import com.example.jewelry_management.enums.OrderStatus;
import com.example.jewelry_management.enums.PaymentMethod;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponse {
    private Integer id;
    private String customerName;
    private String customerPhone;
    private String customerAddress;
    private OrderStatus status;
    private PaymentMethod paymentMethod;
    private BigDecimal totalPrice;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    private List<OrderItemResponse> items;
    private AccountSummaryRes accountResponse;
    private String qrCodeUrl;
}
