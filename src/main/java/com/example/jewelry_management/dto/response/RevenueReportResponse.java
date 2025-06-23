package com.example.jewelry_management.dto.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RevenueReportResponse {
    private String period;
    private BigDecimal totalRevenue;
    private Long orderCount;
}
