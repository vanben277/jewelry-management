package com.example.jewelry_management.form;

import com.example.jewelry_management.enums.GoldType;
import com.example.jewelry_management.enums.ProductStatus;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class FilterProductForm {
    private String name;
    private BigDecimal fromPrice;
    private BigDecimal toPrice;
    private Integer categoryId;
    private ProductStatus status;
    private GoldType goldType;
    private Boolean isDeleted;

    private Integer pageSize = 20;
    private Integer pageNumber = 0;
}
