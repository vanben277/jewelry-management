package com.example.jewelry_management.form;

import com.example.jewelry_management.dto.base.BaseDtoProduct;
import com.example.jewelry_management.enums.GoldType;
import com.example.jewelry_management.enums.ProductStatus;
import com.example.jewelry_management.validator.OnlyOneOfQuantityOrSizes;
import com.example.jewelry_management.validator.QuantitySizeValidationTarget;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@OnlyOneOfQuantityOrSizes
public class UpdateProductForm implements BaseDtoProduct, QuantitySizeValidationTarget {
    @Valid
    List<ProductImageForm> images = new ArrayList<>();

    @Valid
    List<ProductSizeForm> sizes = new ArrayList<>();

    @NotBlank(message = "Trường tên không được bỏ trống")
    private String name;

    @NotNull(message = "Truờng giá không được bỏ trống")
    @DecimalMin(value = "0.01", message = "Giá phải lớn hơn 0")
    private BigDecimal price;

    private Integer quantity;

    @NotNull(message = "Ngày nhập không được bỏ trống")
    @PastOrPresent(message = "Ngày nhập không thể ở tương lai")
    private LocalDate dateOfEntry;

    private String description;

    @NotNull(message = "Trạng thái không được bỏ trống")
    private ProductStatus status;

    @NotNull(message = "Trường thể loại không được bỏ trống")
    private Integer categoryId;

    private GoldType goldType;

}
