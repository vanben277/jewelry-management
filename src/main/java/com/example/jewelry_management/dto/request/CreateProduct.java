package com.example.jewelry_management.dto.request;

import com.example.jewelry_management.dto.base.BaseDtoProduct;
import com.example.jewelry_management.model.ProductStatus;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class CreateProduct implements BaseDtoProduct {
    @NotBlank(message = "Trường tên không được bỏ trống")
    private String name;

    @NotNull(message = "Truờng giá không được bỏ trống")
    @DecimalMin(value = "0.01", message = "Giá phải lớn hơn 0")
    private BigDecimal price;

    @NotNull(message = "Trường số lượng không được bỏ trống")
    @Min(value = 1, message = "Số lượng phải lớn hơn 0")
    private Integer quantity;

    @NotNull(message = "Ngày nhập không được bỏ trống")
    @PastOrPresent(message = "Ngày nhập không thể ở tương lai")
    private LocalDate dateOfEntry;

    @NotBlank(message = "Trường ảnh không được bỏ trống")
    private String image;

    @NotBlank(message = "Trường mô tả không được bỏ trống")
    private String description;

    private ProductStatus status;

    @NotNull(message = "Trường thể loại không được bỏ trống")
    private Integer categoryId;


}
