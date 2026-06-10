package com.example.whitefox.payment.dto;

import com.example.whitefox.payment.enums.PaymentMode;
import lombok.Data;

import java.util.UUID;

@Data
public class CreatePaymentRequest {

    private UUID orderId;

    private Double amount;

    private PaymentMode paymentMode;

    private String transactionReference;

    private String remarks;
}
