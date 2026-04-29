package com.example.jewelry_management.service.impl;

import com.example.jewelry_management.config.VietQRConfig;
import com.example.jewelry_management.service.VietQRService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
@Slf4j
public class VietQRServiceImpl implements VietQRService {

    private final VietQRConfig vietQRConfig;

    @Override
    public String generateQRCodeUrl(Integer orderId, BigDecimal amount, String description) {
        // Format: DH{orderId} - {description}
        String addInfo = String.format("DH%d %s", orderId, description != null ? description : "Thanh toan don hang");

        String encodedAddInfo = URLEncoder.encode(addInfo, StandardCharsets.UTF_8);

        // Build VietQR URL theo format:
        // https://img.vietqr.io/image/{BANK_ID}-{ACCOUNT_NO}-{TEMPLATE}.png?amount={AMOUNT}&addInfo={INFO}&accountName={NAME}
        String qrUrl = UriComponentsBuilder
                .fromHttpUrl(vietQRConfig.getApiUrl())
                .pathSegment(
                    vietQRConfig.getBankId() + "-" +
                    vietQRConfig.getAccountNo() + "-" +
                    vietQRConfig.getTemplate() + ".png"
                )
                .queryParam("amount", amount.longValue())
                .queryParam("addInfo", encodedAddInfo)
                .queryParam("accountName", vietQRConfig.getAccountName())
                .build()
                .toUriString();

        log.info("Generated VietQR URL for order {}: {}", orderId, qrUrl);
        return qrUrl;

    }
    
    private String generateFallbackQRUrl(BigDecimal amount) {
        return UriComponentsBuilder
                .fromHttpUrl(vietQRConfig.getApiUrl())
                .pathSegment(
                    vietQRConfig.getBankId() + "-" + 
                    vietQRConfig.getAccountNo() + "-" + 
                    vietQRConfig.getTemplate() + ".png"
                )
                .queryParam("amount", amount.longValue())
                .queryParam("accountName", vietQRConfig.getAccountName())
                .build()
                .toUriString();
    }
}
