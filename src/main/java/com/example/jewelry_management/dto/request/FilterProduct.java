package com.example.jewelry_management.dto.request;

import com.example.jewelry_management.model.ProductStatus;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class FilterProduct {
    private String name;
    private BigDecimal fromPrice;
    private BigDecimal toPrice;
    private Integer fromQuantity;
    private Integer toQuantity;
    private LocalDate fromDateOfEntry;
    private LocalDate toDateOfEntry;
    private String image;
    private ProductStatus status;
    private Integer categoryId;

    private Integer pageSize = 20;
    private Integer pageNumber = 0;
}
