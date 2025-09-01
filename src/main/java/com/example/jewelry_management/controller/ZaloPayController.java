package com.example.jewelry_management.controller;

import com.example.jewelry_management.service.ZaloPayService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/payment/zalopay")
@RequiredArgsConstructor
public class ZaloPayController {
    private final ZaloPayService zaloPayService;

    @PostMapping("/create")
    public ResponseEntity<String> createPayment(@RequestBody Map<String, Object> body) throws Exception {
        long amount = Long.parseLong(body.get("amount").toString());
        String description = body.get("description").toString();

        String orderUrl = zaloPayService.createOrder(amount, description);
        return ResponseEntity.ok(orderUrl);
    }

    // Callback từ ZaloPay
    @PostMapping("/callback")
    public ResponseEntity<Map<String, Object>> callback(@RequestBody Map<String, Object> body) {
        Map<String, Object> result = new HashMap<>();
        try {
            String data = body.get("data").toString();
            String reqMac = body.get("mac").toString();

            if (zaloPayService.verifyCallback(data, reqMac)) {
                result.put("return_code", 1);
                result.put("return_message", "Success");
            } else {
                result.put("return_code", -1);
                result.put("return_message", "Invalid MAC");
            }
        } catch (Exception e) {
            result.put("return_code", 0);
            result.put("return_message", "Error: " + e.getMessage());
        }
        return ResponseEntity.ok(result);
    }

}
