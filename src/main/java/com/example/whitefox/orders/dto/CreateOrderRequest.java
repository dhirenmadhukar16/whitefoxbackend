package com.example.whitefox.orders.dto;



import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
public class CreateOrderRequest {

    private UUID customerId;

    private UUID storeId;

    private LocalDate pickupDate;

    private LocalDate deliveryDate;

    private List<OrderItemRequest> items;
    
    private String deliveryType; // "RIDER_DELIVERY" or "SELF_PICKUP"
    private String deliveryAddress;
}
