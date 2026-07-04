package com.example.whitefox.adminpanel.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class AdminReviewResponse {

    private UUID reviewId;

    private UUID customerId;
    private String customerName;

    private UUID orderId;
    private String orderNumber;

    private UUID storeId;
    private String storeName;

    private UUID riderId;
    private String riderName;

    private Integer storeRating;
    private Integer riderRating;
    private Integer overallRating;

    private String feedback;

    private LocalDateTime createdAt;
}