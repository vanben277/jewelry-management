package com.example.jewelry_management.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Setter
public class RevenueFilterDto {
    @NotNull(message = "Thời gian không được bỏ trống")
    private String periodType;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private String startDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private String endDate;
}
