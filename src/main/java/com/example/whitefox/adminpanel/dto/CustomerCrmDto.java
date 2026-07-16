package com.example.whitefox.adminpanel.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class CustomerCrmDto {
    private UUID id;
    private String customerCode;
    private String name;
    private String email;
    private String phone;
    private String address;
    private String city;
    private Boolean active;
    private LocalDateTime joinedAt;
    
    // CRM Metrics
    private long totalOrders;
    private double lifetimeValue; // total spent
    private int rewardPoints; // loyalty points
    private String riskScore; // High, Medium, Low
}
