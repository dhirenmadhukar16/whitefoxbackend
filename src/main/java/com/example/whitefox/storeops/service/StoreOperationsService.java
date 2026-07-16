package com.example.whitefox.storeops.service;

//package com.example.whitefox.storeops.service;

import com.example.whitefox.storeops.dto.*;

import java.util.List;
import java.util.UUID;

public interface StoreOperationsService {

    StoreDashboardResponse getDashboard(UUID storeId);

    List<StoreOrderSummaryResponse> getOrdersByStore(UUID storeId);

    List<StoreGarmentResponse> getGarmentsByStore(UUID storeId);

    Object getPickupBillsByStore(UUID storeId);

    StoreOrderSummaryResponse markReceivedFromHq(UUID orderId);

    StoreOrderSummaryResponse markReadyForCustomerPickup(UUID orderId);

    StoreOrderSummaryResponse verifyPickupOtp(UUID orderId, String otp);

    StoreOrderSummaryResponse markOutForDelivery(UUID orderId);

    StoreOrderSummaryResponse markDelivered(UUID orderId);

    List<StoreServicePricingDto> getStorePricing(UUID storeId);

    StoreServicePricingDto setStorePricing(UUID storeId, com.example.whitefox.storeops.dto.SetStorePricingRequest request);

    StoreOrderSummaryResponse settleCash(UUID orderId);

    StoreOrderSummaryResponse settleOnlinePayment(UUID orderId);

    List<com.example.whitefox.riders.dto.RiderResponse> getPendingRiders(UUID storeId);

    com.example.whitefox.riders.dto.RiderResponse approveRider(UUID riderId);

    com.example.whitefox.riders.dto.RiderResponse rejectRider(UUID riderId);
}
