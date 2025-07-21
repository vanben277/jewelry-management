package com.example.jewelry_management.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum AccountStatus {
    ACTIVE, BANNED, INACTIVE, PENDING
}
