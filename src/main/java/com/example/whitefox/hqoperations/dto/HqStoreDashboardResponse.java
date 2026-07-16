package com.example.whitefox.hqoperations.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class HqStoreDashboardResponse {
    private UUID storeId;
    private String storeName;
    private Long orderCount;
    private Long garmentCount;
    private List<HqGarmentResponse> garments;
}
