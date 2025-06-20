package com.example.jewelry_management.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FilterCategory {
    private String name;

    private Integer pageNumber = 0;
    private Integer pageSize = 10;
}
