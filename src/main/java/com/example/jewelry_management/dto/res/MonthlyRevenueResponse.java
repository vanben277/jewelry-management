package com.example.jewelry_management.dto.res;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MonthlyRevenueResponse {
    private BigDecimal totalMoneyMonthly;
    private Integer totalProductMonthly;
    private Integer totalOrderMonthly;
    private Integer totalInventory;
}
