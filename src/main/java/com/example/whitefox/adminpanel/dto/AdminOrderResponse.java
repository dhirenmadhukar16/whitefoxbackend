package com.example.whitefox.adminpanel.dto;

import com.example.whitefox.orders.enums.OrderStatus;
//import com.example.whitefox.payment.enums.PaymentStatus;
import lombok.Builder;
import lombok.Data;
//import com.example.whitefox.payment.enums.PaymentStatus;
import java.util.UUID;
import com.example.whitefox.orders.enums.PaymentStatus;
import com.example.whitefox.orders.enums.PaymentStatus;
@Data
@Builder
public class AdminOrderResponse {

    private UUID orderId;
    private String orderNumber;

    private UUID customerId;
    private String customerName;
    private String customerPhone;

    private UUID storeId;
    private String storeName;

    private UUID deliveryRiderId;
    private String deliveryRiderName;

    private OrderStatus status;
    private PaymentStatus paymentStatus;

    private Double subtotal;
    private Double gst;
    private Double totalAmount;
}