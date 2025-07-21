package com.example.jewelry_management.exception;

public class UnauthorizedException extends BusinessException {
    public UnauthorizedException(String message, String errorCode) {
        super(message, errorCode);
    }
}
