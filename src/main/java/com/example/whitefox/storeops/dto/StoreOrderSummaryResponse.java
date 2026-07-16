package com.example.whitefox.storeops.dto;



import com.example.whitefox.orders.enums.OrderStatus;
import com.example.whitefox.orders.enums.PaymentStatus;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class StoreOrderSummaryResponse {

    private UUID orderId;
    private String orderNumber;

    private UUID customerId;
    private String customerName;
    private String customerPhone;

    private OrderStatus status;
    private PaymentStatus paymentStatus;
    private String deliveryType;
    private String pickupType;
    private String deliveryOtp;
    private String pickupOtp;

    private Double totalAmount;
}
