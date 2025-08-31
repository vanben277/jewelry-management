package com.example.jewelry_management.dto.res;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MonthRevenueListResponse {
    private String month;
    private Double revenue;
}
