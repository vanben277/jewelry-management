package com.example.jewelry_management.form;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TopProductFilterForm {
    @NotNull(message = "Số lượng sản phẩm không được bỏ trống!")
    @Min(value = 1, message = "topN phải lớn hơn 0")
    private Integer topN = 5;
}
