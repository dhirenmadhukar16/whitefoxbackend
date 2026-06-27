package com.example.whitefox.storehistory.dto;

import com.example.whitefox.orders.enums.OrderStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Data
public class StoreOrderHistoryResponse {

    private UUID orderId;

    private String orderNumber;

    private String customerName;

    private String customerPhone;

    private String riderName;

    private Double amount;

    private OrderStatus status;

    private LocalDateTime bookingTime;

    private LocalDateTime pickupTime;

    private LocalDateTime deliveryTime;

}