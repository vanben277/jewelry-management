package com.example.jewelry_management.form;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FilterCategoryForm {
    private String name;

    private Integer pageNumber = 0;
    private Integer pageSize = 10;
}
