package com.example.whitefox.storeops.controller;


import com.example.whitefox.auth.repository.UserRepository;
import com.example.whitefox.store.dto.StoreResponse;
import com.example.whitefox.store.service.StoreService;
import com.example.whitefox.storeops.dto.*;
import com.example.whitefox.storeops.service.StoreOperationsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/store-ops")
@RequiredArgsConstructor
public class StoreOperationsController {

    private final StoreOperationsService storeOperationsService;
    private final com.example.whitefox.tracking.service.BagService bagService;
    private final StoreService storeService;
    private final UserRepository userRepository;

    @PostMapping("/bags/pack")
    public com.example.whitefox.tracking.dto.BagResponse packBag(
            @RequestBody com.example.whitefox.tracking.dto.CreateBagRequest request) {
        
        return bagService.createBag(request);
    }

    @PostMapping("/bags/receive")
    public com.example.whitefox.tracking.dto.BagResponse receiveBag(
            @RequestParam String qrCode) {
        
        com.example.whitefox.tracking.dto.BagResponse bag = bagService.getBagByQrCode(qrCode);
        return bagService.updateBagStatus(bag.getId(), "UNPACKED_AT_STORE");
    }

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

    /**
     * Returns the current logged-in user's store details.
     * Uses the JWT principal to look up the user, then fetches their storeId.
     */
    @GetMapping("/my-store")
    public StoreResponse getMyStore(Principal principal) {
        if (principal == null) {
            throw new RuntimeException("Not authenticated");
        }
        var user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (user.getStoreId() == null) {
            throw new RuntimeException("No store linked to this user account");
        }
        return storeService.getStore(user.getStoreId());
    }
}
