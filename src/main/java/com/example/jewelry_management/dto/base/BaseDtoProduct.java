package com.example.jewelry_management.dto.base;

import com.example.jewelry_management.model.ProductStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface BaseDtoProduct {
    String getName();

    BigDecimal getPrice();

    Integer getQuantity();

    LocalDate getDateOfEntry();

    String getImage();

    String getDescription();

    ProductStatus getStatus();

    Integer getCategoryId();
}
