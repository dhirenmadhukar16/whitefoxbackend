package com.example.whitefox.payment.dto;

import com.example.whitefox.payment.enums.PaymentMode;
import com.example.whitefox.payment.enums.PaymentTransactionStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class PaymentResponse {

    private UUID id;

    private UUID orderId;

    private String orderNumber;

    private Double amount;

    private PaymentMode paymentMode;

    private PaymentTransactionStatus status;

    private String transactionReference;

    private String remarks;

    private LocalDateTime paidAt;
}
