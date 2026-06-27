package com.example.whitefox.notifications.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SmsServiceImpl implements SmsService {

    @Override
    public void sendOtp(String phone, String otp) {

        // For now logs OTP.
        // Later replace this with MSG91 / Twilio / Fast2SMS API.
        log.info("Sending OTP {} to phone {}", otp, phone);

        /*
        Production:
        Call SMS Gateway API here.
        Example providers:
        - MSG91
        - Twilio
        - Fast2SMS
        - Gupshup WhatsApp
        */
    }
}