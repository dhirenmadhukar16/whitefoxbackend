package com.example.whitefox.storeops.dto;



import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class StoreDashboardResponse {

    private UUID storeId;
    private String storeName;

    private Long totalOrders;
    private Long activeOrders;
    private Long readyOrders;
    private Long deliveredOrders;

    private Long pendingPickupBills;
    private Long totalGarments;
    private Long garmentsAtStore;
    private Long garmentsReady;

    private Long totalEmployees;
    private Long activeEmployees;

    private Double totalRevenue;
}
