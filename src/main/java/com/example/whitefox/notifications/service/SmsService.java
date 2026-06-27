package com.example.whitefox.notifications.service;

public interface SmsService {
    void sendOtp(String phone, String otp);
}