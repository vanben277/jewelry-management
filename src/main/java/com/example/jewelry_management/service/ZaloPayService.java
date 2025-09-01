package com.example.jewelry_management.service;


public interface ZaloPayService {
    String createOrder(long amount, String description) throws Exception;

    boolean verifyCallback(String data, String reqMac) throws Exception;
}

