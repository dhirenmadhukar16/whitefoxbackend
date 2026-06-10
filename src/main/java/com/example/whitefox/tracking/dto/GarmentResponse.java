package com.example.whitefox.tracking.dto;



import com.example.whitefox.tracking.enums.GarmentStatus;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class GarmentResponse {

    private UUID id;

    private UUID orderId;

    private String storeQrCode;

    private String outingQrCode;

    private String itemName;

    private String serviceType;

    private String color;

    private String conditionNote;

    private String photoUrl;

    private GarmentStatus status;

    private List<TrackingHistoryResponse> history;
}
