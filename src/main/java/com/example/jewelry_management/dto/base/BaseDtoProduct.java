package com.example.jewelry_management.dto.base;

import com.example.jewelry_management.enums.GoldType;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface BaseDtoProduct {
    String getName();

    BigDecimal getPrice();

    Integer getQuantity();

    LocalDate getDateOfEntry();

    String getDescription();


    Integer getCategoryId();

    GoldType getGoldType();

}
