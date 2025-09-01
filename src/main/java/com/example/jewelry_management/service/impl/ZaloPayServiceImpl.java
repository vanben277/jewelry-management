package com.example.jewelry_management.service.impl;

import com.example.jewelry_management.service.ZaloPayService;
import com.example.jewelry_management.utils.HmacSHA256Util;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class ZaloPayServiceImpl implements ZaloPayService {

    private final RestTemplate restTemplate = new RestTemplate();
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

    @Override
    public String createOrder(long amount, String description) throws Exception {
        try {
            System.out.println("=== BẮT ĐẦU TẠO ORDER ZALOPAY ===");

            // config
            System.out.println("App ID: " + appId);
            System.out.println("Endpoint: " + endpoint);
            System.out.println("Callback URL: " + callbackUrl);

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

            // Sửa lại phần tạo data để tính MAC theo đúng format ZaloPay
            String data = appId + "|" + appTransId + "|" + "demoUser" + "|" + amount + "|" + appTime + "|" + "{}" + "|" + "[]";
            System.out.println("Data for MAC: " + data);

            String mac = HmacSHA256Util.sign(data, key1);
            order.put("mac", mac);

            System.out.println("Generated MAC: " + mac);
            System.out.println("Order payload: " + order);

            System.out.println("Đang gọi ZaloPay API...");
            ResponseEntity<Map> response = restTemplate.postForEntity(endpoint, order, Map.class);

            System.out.println("Response status: " + response.getStatusCode());
            System.out.println("ZaloPay response: " + response.getBody());

            if (response.getBody() != null && response.getBody().get("order_url") != null) {
                String orderUrl = response.getBody().get("order_url").toString();
                System.out.println("Order URL: " + orderUrl);
                return orderUrl;
            }

            if (response.getBody() != null) {
                System.out.println("Response body: " + response.getBody());
                if (response.getBody().get("return_code") != null) {
                    System.out.println("Return code: " + response.getBody().get("return_code"));
                    System.out.println("Return message: " + response.getBody().get("return_message"));
                    if (response.getBody().get("sub_return_code") != null) {
                        System.out.println("Sub return code: " + response.getBody().get("sub_return_code"));
                        System.out.println("Sub return message: " + response.getBody().get("sub_return_message"));
                    }
                }
            }

            throw new RuntimeException("Không thể tạo link thanh toán ZaloPay! Response: " + response.getBody());

        } catch (Exception e) {
            System.out.println("=== LỖI TẠO ORDER ===");
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public boolean verifyCallback(String data, String reqMac) throws Exception {
        String mac = HmacSHA256Util.sign(data, key2);
        return mac.equals(reqMac);
    }
}