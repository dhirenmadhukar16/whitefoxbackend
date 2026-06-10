package com.example.whitefox.orders.service;



import com.example.whitefox.orders.dto.CreateOrderRequest;
import com.example.whitefox.orders.dto.OrderResponse;

import java.util.List;
import java.util.UUID;

public interface OrderService {

    OrderResponse createOrder(CreateOrderRequest request);

    OrderResponse getOrder(UUID orderId);

    List<OrderResponse> getAllOrders();
}