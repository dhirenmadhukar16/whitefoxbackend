package com.example.whitefox.DeliveryOtp.service;



import com.example.whitefox.DeliveryOtp.dto.GenerateOtpResponse;
import com.example.whitefox.DeliveryOtp.dto.VerifyDeliveryOtpRequest;

import java.util.UUID;

public interface DeliveryOtpService {

    GenerateOtpResponse generateOtp(UUID orderId);

    void verifyOtp(UUID orderId, VerifyDeliveryOtpRequest request);
}