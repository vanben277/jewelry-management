package com.example.jewelry_management.exception;

public class ForbiddenException extends BusinessException {
    public ForbiddenException(String message, String errorCode) {
        super(message, errorCode);
    }
}
