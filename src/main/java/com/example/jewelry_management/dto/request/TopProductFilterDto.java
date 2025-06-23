package com.example.jewelry_management.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Setter
public class TopProductFilterDto {
    @NotNull(message = "Số lượng sản phẩm không được bỏ trống!")
    @Min(value = 1, message = "topN phải lớn hơn 0")
    private Integer topN = 5;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private String startDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private String endDate;
    private Integer categoryId;
}
