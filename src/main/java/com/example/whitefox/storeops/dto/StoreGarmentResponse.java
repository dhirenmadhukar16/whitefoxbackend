package com.example.whitefox.storeops.dto;



import com.example.whitefox.tracking.enums.GarmentStatus;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class StoreGarmentResponse {

    private UUID garmentId;
    private UUID orderId;
    private String orderNumber;

    private String itemName;
    private String serviceType;
    private String storeQrCode;

    private GarmentStatus status;
}
