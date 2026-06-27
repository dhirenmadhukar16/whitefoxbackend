package com.example.whitefox.customerreview.dto;



import lombok.Data;

import java.util.UUID;

@Data
public class CreateCustomerReviewRequest {

    private UUID customerId;

    private UUID orderId;

    private UUID storeId;

    private UUID riderId;

    private Integer storeRating;

    private Integer riderRating;

    private Integer overallRating;

    private String feedback;
}
