package com.example.whitefox.payment.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class RazorpayVerifyBookingPaymentRequest {
    private UUID bookingId;
    private String razorpayPaymentId;
    private String razorpayOrderId;
    private String razorpaySignature;
}
