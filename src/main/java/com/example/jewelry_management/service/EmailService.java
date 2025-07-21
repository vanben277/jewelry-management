package com.example.jewelry_management.service;

public interface EmailService {
    void sendOtpEmailHtml(String to, String otp);

    void sendPasswordChangedConfirmationHtml(String to);
}
