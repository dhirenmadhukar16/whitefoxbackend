package com.example.whitefox.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class OtpService {

    private final SmsService smsService;
    
    // In-memory cache for OTPs to avoid Couchbase timeouts
    private final Map<String, OtpData> otpCache = new ConcurrentHashMap<>();

    private static class OtpData {
        String otp;
        long expiryTimestamp;
        boolean verified;

        OtpData(String otp, long expiry) {
            this.otp = otp;
            this.expiryTimestamp = expiry;
            this.verified = false;
        }
    }

    public void sendOtp(String phone, String role) {
        // Hardcoded to 2323 because the user's SMS template on SMSIndiaHub is locked to this exact message
        String otp = "2323"; 
        
        String key = "otp:auth:" + role + ":" + phone;
        
        otpCache.put(key, new OtpData(otp, Instant.now().getEpochSecond() + 300));
        
        smsService.sendOtp(phone, otp);
    }

    public boolean verifyOtp(String phone, String role, String otp) {
        String key = "otp:auth:" + role + ":" + phone;
        
        OtpData doc = otpCache.get(key);
        if (doc != null) {
            if (doc.otp.equals(otp) && !doc.verified && doc.expiryTimestamp > Instant.now().getEpochSecond()) {
                doc.verified = true;
                return true;
            }
        }
        return false;
    }
}
