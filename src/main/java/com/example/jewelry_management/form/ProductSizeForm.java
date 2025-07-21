package com.example.jewelry_management.form;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductSizeForm {

    @NotNull(message = "Kích cỡ không được bỏ trống")
    @Min(value = 1, message = "Kích cỡ phải lớn hơn 0")
    private Integer size;

    @NotNull(message = "Số lượng không được bỏ trống")
    @Min(value = 1, message = "Số lượng phải lớn hơn 0")
    private Integer quantity;

}
