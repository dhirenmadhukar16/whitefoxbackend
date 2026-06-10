package com.example.whitefox.report.dto;



import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReportsResponse {

    private Long totalOrders;

    private Long deliveredOrders;

    private Long pendingOrders;

    private Long totalGarments;

    private Long deliveredGarments;

    private Long pendingGarments;

    private Double totalRevenue;

    private Double totalPaymentsCollected;
}
