package com.example.whitefox.report.dto;



import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class StoreReportResponse {

    private UUID storeId;

    private String storeName;

    private Long totalOrders;

    private Long deliveredOrders;

    private Long pendingOrders;

    private Long totalGarments;

    private Double revenue;
}
