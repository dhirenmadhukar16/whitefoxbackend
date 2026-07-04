package com.example.whitefox.payment.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class RazorpayCreateOrderRequest {
    private UUID orderId;
    private Double amount;
    private String paymentMode; // FULL_ONLINE or HALF_ADVANCE
}