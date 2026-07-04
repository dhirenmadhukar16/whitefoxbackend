package com.example.whitefox.DeliveryOtp.controller;

import com.example.whitefox.DeliveryOtp.dto.GenerateOtpResponse;
import com.example.whitefox.DeliveryOtp.dto.VerifyDeliveryOtpRequest;
import com.example.whitefox.DeliveryOtp.service.DeliveryOtpService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/delivery-otp")
@RequiredArgsConstructor
public class DeliveryOtpController {

    private final DeliveryOtpService deliveryOtpService;

    @PostMapping("/orders/{orderId}/generate")
    public GenerateOtpResponse generateOtp(
            @PathVariable UUID orderId
    ) {
        return deliveryOtpService.generateOtp(orderId);
    }

    @PostMapping("/orders/{orderId}/verify")
    public String verifyOtp(
            @PathVariable UUID orderId,
            @RequestBody VerifyDeliveryOtpRequest request
    ) {
        deliveryOtpService.verifyOtp(orderId, request);
        return "Order delivered successfully";
    }
}