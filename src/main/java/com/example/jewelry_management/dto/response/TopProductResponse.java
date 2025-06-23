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
public class TopProductResponse {
    private Integer productId;
    private String name;
    private Long totalQuantitySold;
    private BigDecimal totalRevenue;
}
