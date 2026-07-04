package com.example.whitefox.invoice.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class InvoiceResponse {

    private UUID id;
    private String invoiceNumber;

    private UUID orderId;
    private String orderNumber;

    private String customerName;

    private Double subtotal;
    private Double gst;
    private Double totalAmount;

    private LocalDateTime generatedAt;
}
