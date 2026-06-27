package com.example.whitefox.Riderdelivery.service;



import com.example.whitefox.orders.dto.OrderResponse;

import java.util.List;
import java.util.UUID;

public interface RiderDeliveryService {

    List<OrderResponse> getDeliveryOrders(UUID riderId);

    OrderResponse assignDelivery(UUID orderId, UUID riderId);

    OrderResponse markOutForDelivery(UUID orderId);

    OrderResponse markDelivered(UUID orderId);

    OrderResponse markDeliveryFailed(UUID orderId);
}
