package com.example.whitefox.Riderdelivery.controller;



import com.example.whitefox.customerbooking.dto.CustomerBookingResponse;
import com.example.whitefox.orders.dto.OrderResponse;
import com.example.whitefox.Riderdelivery.service.RiderDeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.example.whitefox.customerbooking.service.CustomerBookingService;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/rider")
@RequiredArgsConstructor
public class RiderDeliveryController {

    private final RiderDeliveryService riderDeliveryService;

    @GetMapping("/{riderId}/delivery-orders")
    public List<OrderResponse> getDeliveryOrders(
            @PathVariable UUID riderId
    ) {
        return riderDeliveryService.getDeliveryOrders(riderId);
    }

    @PatchMapping("/orders/{orderId}/assign-delivery/{riderId}")
    public OrderResponse assignDelivery(
            @PathVariable UUID orderId,
            @PathVariable UUID riderId
    ) {
        return riderDeliveryService.assignDelivery(orderId, riderId);
    }

    @PatchMapping("/orders/{orderId}/out-for-delivery")
    public OrderResponse markOutForDelivery(
            @PathVariable UUID orderId
    ) {
        return riderDeliveryService.markOutForDelivery(orderId);
    }

    @PatchMapping("/orders/{orderId}/delivered")
    public OrderResponse markDelivered(
            @PathVariable UUID orderId
    ) {
        return riderDeliveryService.markDelivered(orderId);
    }

    @PatchMapping("/orders/{orderId}/delivery-failed")
    public OrderResponse markDeliveryFailed(
            @PathVariable UUID orderId
    ) {
        return riderDeliveryService.markDeliveryFailed(orderId);
    }

}