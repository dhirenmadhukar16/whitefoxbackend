package com.example.whitefox.adminpanel.dto;

import lombok.Builder;
import lombok.Data;
import java.util.List;

import java.util.Map;

@Data
@Builder
public class AdminStoreAnalyticsResponse {

    private List<DailyMetric> dailyOrders;
    private List<DailyMetric> dailyRevenue;
    private List<ServiceMetric> servicesBreakdown;
    private Map<String, Double> revenueBreakdown;

    @Data
    @Builder
    public static class DailyMetric {
        private String date; // Format: YYYY-MM-DD
        private Double value;
    }

    @Data
    @Builder
    public static class ServiceMetric {
        private String serviceName;
        private Long count;
    }
}
