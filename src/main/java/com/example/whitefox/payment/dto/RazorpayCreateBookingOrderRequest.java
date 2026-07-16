package com.example.whitefox.payment.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class RazorpayCreateBookingOrderRequest {
    private UUID bookingId;
    private Double amount;
    private String paymentMode; // FULL_ONLINE, HALF_ADVANCE, etc.
}
