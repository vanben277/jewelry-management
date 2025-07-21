package com.example.jewelry_management.dto.res;

import com.example.jewelry_management.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {
    private Integer id;
    private String customerName;
    private String customerPhone;
    private String customerAddress;
    private OrderStatus status;
    private BigDecimal totalPrice;
    private Boolean isDeleted;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    private List<OrderItemResponse> items;
    private AccountSummaryRes accountResponse;
}
