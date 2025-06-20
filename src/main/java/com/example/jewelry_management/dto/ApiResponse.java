package com.example.jewelry_management.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiResponse {
    private String message;
    private Object data;
    private String errorMessage;

    public ApiResponse(String message, Object data) {
        this.message = message;
        this.data = data;
    }

    public ApiResponse(String message, Object data, String errorMessage) {
        this.message = message;
        this.data = data;
        this.errorMessage = errorMessage;
    }
}
