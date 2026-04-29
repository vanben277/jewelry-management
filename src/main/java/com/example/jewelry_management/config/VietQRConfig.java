package com.example.jewelry_management.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "vietqr")
@Getter
@Setter
public class VietQRConfig {
    
    /**
     * Mã ngân hàng (Bank ID)
     */
    private String bankId;
    
    /**
     * Số tài khoản ngân hàng
     */
    private String accountNo;
    
    /**
     * Tên chủ tài khoản
     */
    private String accountName;
    
    /**
     * Template QR code: compact, compact2, qr_only, print
     */
    private String template;
    
    /**
     * API URL của VietQR
     */
    private String apiUrl;
}
