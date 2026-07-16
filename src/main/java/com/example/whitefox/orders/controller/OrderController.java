package com.example.whitefox.orders.controller;



import com.example.whitefox.orders.dto.CreateOrderRequest;
import com.example.whitefox.orders.dto.OrderResponse;
import com.example.whitefox.orders.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public OrderResponse createOrder(
            @RequestBody CreateOrderRequest request
    ) {
        return orderService.createOrder(request);
    }

    @GetMapping
    public List<OrderResponse> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/{id}")
    public OrderResponse getOrder(
            @PathVariable UUID id
    ) {
        return orderService.getOrder(id);
    }

    @PatchMapping("/{id}/delivery-type")
    public OrderResponse changeDeliveryType(
            @PathVariable UUID id,
            @RequestParam String deliveryType
    ) {
        return orderService.changeDeliveryType(id, deliveryType);
    }
}