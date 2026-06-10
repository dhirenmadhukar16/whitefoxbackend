package com.example.whitefox.payment.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentSummaryResponse {

    private Long totalTransactions;

    private Double totalCollected;

    private Double cashCollected;

    private Double upiCollected;

    private Double cardCollected;

    private Double onlineCollected;
}
