package com.example.jewelry_management.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum GoldType {
    GOLD_10K, GOLD_14K, GOLD_18K, GOLD_24K


}
