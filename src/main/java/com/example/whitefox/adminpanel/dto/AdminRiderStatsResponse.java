package com.example.whitefox.adminpanel.dto;



import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class AdminRiderStatsResponse {

    private UUID riderId;
    private String riderName;
    private String riderCode;
    private String phone;
    private String status;

    private Long deliveryOrders;
    private Long pickupBills;

    private Double latitude;
    private Double longitude;
}