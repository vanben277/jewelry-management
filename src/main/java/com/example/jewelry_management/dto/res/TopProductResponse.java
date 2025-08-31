package com.example.jewelry_management.dto.res;


import com.example.jewelry_management.form.ProductImageForm;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TopProductResponse {
    private Integer productId;
    private String name;
    private BigDecimal price;
    private Long totalQuantitySold;
    private List<ProductImageForm> images = new ArrayList<>();
}

