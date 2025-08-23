package com.example.jewelry_management.form;

import com.example.jewelry_management.enums.ProductStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateProductStatusForm {
    @NotNull(message = "Trạng thái sản phẩm không được bỏ trống")
    private ProductStatus status;
}
