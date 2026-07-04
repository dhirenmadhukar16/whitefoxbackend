package com.example.whitefox.adminpanel.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdminDashboardResponse {

    private Long totalStores;
    private Long totalCustomers;
    private Long totalRiders;
    private Long totalOrders;

    private Long activeOrders;
    private Long deliveredOrders;
    private Long cancelledOrders;

    private Long totalGarments;

    private Long pendingPickupBills;
    private Long pendingCustomerBookings;

    private Long totalEmployees;

    private Double totalRevenue;
    private Double todayRevenue;

    private Long todayOrders;
    private Long todayBookings;
}