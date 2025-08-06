package com.example.jewelry_management.form;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemRequestForm {
    @NotNull(message = "Trường productId không được để trống")
    @Min(value = 1, message = "Trường productId phải lớn hơn 0")
    private Integer productId;

    @NotNull(message = "Trường quantity không được để trống")
    @Min(value = 1, message = "Trường quantity phải lớn hơn 0")
    private Integer quantity;

    private Integer size;
}
