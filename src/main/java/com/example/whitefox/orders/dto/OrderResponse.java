package com.example.whitefox.orders.dto;



import com.example.whitefox.orders.enums.OrderStatus;
import com.example.whitefox.orders.enums.PaymentStatus;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class OrderResponse {

    private UUID id;

    private String orderNumber;

    private UUID customerId;

    private String customerName;

    private UUID storeId;

    private String storeName;

    private OrderStatus status;

    private PaymentStatus paymentStatus;

    private Double subtotal;

    private Double gst;

    private Double totalAmount;

    private List<OrderItemResponse> items;
}