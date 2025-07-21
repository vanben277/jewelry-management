package com.example.jewelry_management.form;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class FilterProductForm {
    private String name;
    private BigDecimal fromPrice;
    private BigDecimal toPrice;
    private LocalDate fromDateOfEntry;
    private LocalDate toDateOfEntry;
    private Integer categoryId;

    private Integer pageSize = 20;
    private Integer pageNumber = 0;
}
