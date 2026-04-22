package com.example.jewelry_management.service.impl;

import com.example.jewelry_management.service.ZaloPayService;
import com.example.jewelry_management.utils.HmacSHA256Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
@Slf4j
public class ZaloPayServiceImpl implements ZaloPayService {

    private final RestTemplate restTemplate;
    @Value("${zalopay.appid}")
    private String appId;
    @Value("${zalopay.key1}")
    private String key1;
    @Value("${zalopay.key2}")
    private String key2;
    @Value("${zalopay.endpoint}")
    private String endpoint;
    @Value("${zalopay.callback-url}")
    private String callbackUrl;

    public ZaloPayServiceImpl(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder
                .setConnectTimeout(Duration.ofSeconds(5))
                .setReadTimeout(Duration.ofSeconds(10))
                .build();
    }

    @Override
    public String createOrder(long amount, String description) throws Exception {
        try {
            long appTime = System.currentTimeMillis();
            String appTransId = new SimpleDateFormat("yyMMdd").format(new Date()) + "_" + appTime;

            Map<String, Object> order = new LinkedHashMap<>();
            order.put("app_id", Integer.parseInt(appId));
            order.put("app_trans_id", appTransId);
            order.put("app_user", "demoUser");
            order.put("app_time", appTime);
            order.put("amount", amount);
            order.put("item", "[]");
            order.put("embed_data", "{}");
            order.put("description", description);
            order.put("bank_code", "zalopayapp");
            order.put("callback_url", callbackUrl);

            String data = appId + "|" + appTransId + "|" + "demoUser" + "|" + amount + "|" + appTime + "|" + "{}" + "|" + "[]";
            String mac = HmacSHA256Util.sign(data, key1);
            order.put("mac", mac);

            log.info("Creating ZaloPay order. appTransId={}, amount={}", appTransId, amount);

            ResponseEntity<Map> response;
            try {
                response = restTemplate.postForEntity(endpoint, order, Map.class);
            } catch (RestClientException ex) {
                log.error("ZaloPay API call failed. appTransId={}", appTransId, ex);
                throw new RuntimeException("Không thể kết nối ZaloPay. Vui lòng thử lại sau.");
            }

            HttpStatusCode status = response.getStatusCode();
            if (!status.is2xxSuccessful()) {
                log.error("ZaloPay API non-2xx. appTransId={}, status={}", appTransId, status.value());
                throw new RuntimeException("ZaloPay phản hồi lỗi. Vui lòng thử lại sau.");
            }

            if (response.getBody() != null && response.getBody().get("order_url") != null) {
                String orderUrl = response.getBody().get("order_url").toString();
                log.info("ZaloPay order created successfully. appTransId={}", appTransId);
                return orderUrl;
            }

            Object returnCode = response.getBody() != null ? response.getBody().get("return_code") : null;
            Object returnMessage = response.getBody() != null ? response.getBody().get("return_message") : null;
            log.error("ZaloPay create order failed. appTransId={}, return_code={}, return_message={}",
                    appTransId, returnCode, returnMessage);
            throw new RuntimeException("Không thể tạo link thanh toán ZaloPay. Vui lòng thử lại sau.");

        } catch (Exception e) {
            throw e;
        }
    }

    public boolean verifyCallback(String data, String reqMac) throws Exception {
        String mac = HmacSHA256Util.sign(data, key2);
        return mac.equals(reqMac);
    }
}