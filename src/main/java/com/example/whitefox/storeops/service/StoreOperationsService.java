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

    StoreOrderSummaryResponse markOutForDelivery(UUID orderId);

    StoreOrderSummaryResponse markDelivered(UUID orderId);
}
