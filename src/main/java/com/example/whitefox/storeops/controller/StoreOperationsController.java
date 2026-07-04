package com.example.whitefox.storeops.controller;


import com.example.whitefox.storeops.dto.*;
import com.example.whitefox.storeops.service.StoreOperationsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/store-ops")
@RequiredArgsConstructor
public class StoreOperationsController {

    private final StoreOperationsService storeOperationsService;

    @GetMapping("/stores/{storeId}/dashboard")
    public StoreDashboardResponse getDashboard(
            @PathVariable UUID storeId
    ) {
        return storeOperationsService.getDashboard(storeId);
    }

    @GetMapping("/stores/{storeId}/orders")
    public List<StoreOrderSummaryResponse> getOrdersByStore(
            @PathVariable UUID storeId
    ) {
        return storeOperationsService.getOrdersByStore(storeId);
    }

    @GetMapping("/stores/{storeId}/garments")
    public List<StoreGarmentResponse> getGarmentsByStore(
            @PathVariable UUID storeId
    ) {
        return storeOperationsService.getGarmentsByStore(storeId);
    }

    @GetMapping("/stores/{storeId}/pickup-bills")
    public Object getPickupBillsByStore(
            @PathVariable UUID storeId
    ) {
        return storeOperationsService.getPickupBillsByStore(storeId);
    }

    @PatchMapping("/orders/{orderId}/received-from-hq")
    public StoreOrderSummaryResponse markReceivedFromHq(
            @PathVariable UUID orderId
    ) {
        return storeOperationsService.markReceivedFromHq(orderId);
    }

    @PatchMapping("/orders/{orderId}/ready-for-pickup")
    public StoreOrderSummaryResponse markReadyForCustomerPickup(
            @PathVariable UUID orderId
    ) {
        return storeOperationsService.markReadyForCustomerPickup(orderId);
    }

    @PatchMapping("/orders/{orderId}/out-for-delivery")
    public StoreOrderSummaryResponse markOutForDelivery(
            @PathVariable UUID orderId
    ) {
        return storeOperationsService.markOutForDelivery(orderId);
    }

    @PatchMapping("/orders/{orderId}/delivered")
    public StoreOrderSummaryResponse markDelivered(
            @PathVariable UUID orderId
    ) {
        return storeOperationsService.markDelivered(orderId);
    }

    @GetMapping("/stores/{storeId}/pricing")
    public List<StoreServicePricingDto> getStorePricing(
            @PathVariable UUID storeId
    ) {
        return storeOperationsService.getStorePricing(storeId);
    }

    @PostMapping("/stores/{storeId}/pricing")
    public StoreServicePricingDto setStorePricing(
            @PathVariable UUID storeId,
            @RequestBody SetStorePricingRequest request
    ) {
        return storeOperationsService.setStorePricing(storeId, request);
    }
}
