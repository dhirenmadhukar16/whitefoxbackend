package com.example.whitefox.hqoperations.dto;

import com.example.whitefox.tracking.enums.GarmentStatus;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class HqGarmentResponse {

    private UUID garmentId;

    private UUID orderId;

    private String orderNumber;

    private UUID storeId;

    private String storeName;

    private String itemName;

    private String serviceType;

    private String storeQrCode;

    private String outingQrCode;

    private GarmentStatus status;
}