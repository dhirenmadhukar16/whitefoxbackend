package com.example.whitefox.adminpanel.dto;



import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class AdminStoreStatsResponse {

    private UUID storeId;
    private String storeName;
    private String city;
    private String address;
    private String loginEmail;

    private Long totalOrders;
    private Long activeOrders;
    private Long deliveredOrders;

    private Long totalEmployees;
    private Long totalPickupBills;
    private Long pendingPickupBills;

    private Double totalRevenue;
}
