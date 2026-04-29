package com.example.jewelry_management.service;

import java.math.BigDecimal;

public interface VietQRService {
    
    /**
     * Generate VietQR URL cho chuyển khoản
     * 
     * @param orderId ID đơn hàng
     * @param amount Số tiền cần chuyển
     * @param description Nội dung chuyển khoản
     * @return URL của QR code image
     */
    String generateQRCodeUrl(Integer orderId, BigDecimal amount, String description);
}
