package com.example.whitefox.adminpanel.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class AdminCustomerStatsResponse {

    private UUID customerId;

    private String customerName;

    private String phone;

    private String email;

    private Long totalOrders;

    private Long activeOrders;

    private Long totalBookings;

    private Long savedAddresses;

    private Long reviews;

    private Double totalSpend;
}