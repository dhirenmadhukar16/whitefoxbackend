package com.example.whitefox.payment.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class RazorpayVerifyPaymentRequest {
    private UUID orderId;
    private String razorpayOrderId;
    private String razorpayPaymentId;
    private String razorpaySignature;
    private Double amount;
    private String paymentMode;
}