package com.example.jewelry_management.form;

import com.example.jewelry_management.enums.GoldType;
import com.example.jewelry_management.model.Category;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class FilterProductsByCategoryForm {
    private String name;
    private GoldType goldType;
    private BigDecimal fromPrice;
    private BigDecimal toPrice;

    private Integer pageSize = 5;
    private Integer pageNumber = 0;

    private String sortBy = "dateOfEntry";
    private String sortDirection = "desc";
}
