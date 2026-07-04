package com.example.whitefox.payment.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class RazorpayCreateOrderResponse {
    private UUID orderId;
    private String razorpayOrderId;
    private String keyId;
    private Double amount;
    private Integer amountInPaise;
    private String currency;
}