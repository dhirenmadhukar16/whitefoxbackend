package com.example.whitefox.adminpanel.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdminFinanceStatsResponse {

    private Double totalRevenue;

    private Double todayRevenue;

    private Long totalPayments;

    private Long paidOrders;

    private Long unpaidOrders;

    private Long partialPaidOrders;

    private Double pendingAmount;

    private Long totalInvoices;
}