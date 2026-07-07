package com.example.whitefox.adminpanel.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class AdminRiderDetailResponse {
    private UUID riderId;
    private String riderName;
    private String riderCode;
    private String phone;
    private String email;
    private String status;
    private String vehicleNumber;
    private Double latitude;
    private Double longitude;
    private String loginEmail;
    
    private Long totalDeliveries;
    private Long totalPickups;
    
    // To be expanded as needed for ride history
    private List<Object> recentActivity; 
}
