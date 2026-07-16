package com.example.whitefox.notifications.service;

import com.example.whitefox.DeliveryOtp.dto.GenerateOtpResponse;
import com.example.whitefox.DeliveryOtp.dto.VerifyDeliveryOtpRequest;
import com.example.whitefox.DeliveryOtp.service.DeliveryOtpService;
import com.example.whitefox.notifications.service.SmsService;
import com.example.whitefox.orders.entity.LaundryOrder;
import com.example.whitefox.orders.enums.OrderStatus;
import com.example.whitefox.orders.repository.LaundryOrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeliveryOtpServiceImpl implements DeliveryOtpService {

    private final LaundryOrderRepository laundryOrderRepository;
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

    @Override
    public GenerateOtpResponse generateOtp(UUID orderId) {
        LaundryOrder order = laundryOrderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        String phone = order.getCustomer().getPhone();
        if (phone == null || phone.isBlank()) {
            throw new RuntimeException("Customer phone not found");
        }

        // Hardcoded to 2323 because the user's SMS template on SMSIndiaHub is locked to this exact message
        String otp = "2323"; 
        
        String key = "otp:delivery:" + orderId.toString();
        
        otpCache.put(key, new OtpData(otp, Instant.now().getEpochSecond() + 600)); // 10 minutes
        
        smsService.sendOtp(phone, otp);

        GenerateOtpResponse response = new GenerateOtpResponse();
        response.setOtp(null); 
        response.setMessage("OTP sent to customer's phone");
        return response;
    }

    @Override
    public void verifyOtp(UUID orderId, VerifyDeliveryOtpRequest request) {
        String key = "otp:delivery:" + orderId.toString();
        
        OtpData doc = otpCache.get(key);
        if (doc == null) {
            throw new RuntimeException("OTP not found or expired");
        }

        if (doc.verified) {
            throw new RuntimeException("OTP already verified");
        }

        if (doc.expiryTimestamp < Instant.now().getEpochSecond()) {
            throw new RuntimeException("OTP expired");
        }

        if (!doc.otp.equals(request.getOtp())) {
            throw new RuntimeException("Invalid OTP");
        }

        doc.verified = true;

        LaundryOrder order = laundryOrderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(OrderStatus.DELIVERED);
        laundryOrderRepository.save(order);
    }
}