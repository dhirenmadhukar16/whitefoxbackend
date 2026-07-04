package com.example.whitefox.pickupbill.dto;

import lombok.Data;
import java.util.List;
import java.util.UUID;

@Data
public class CreatePickupBillRequest {

    private UUID customerId;
    private UUID riderId;

    // rider's original / assigned store
    private UUID storeId;
    private UUID customerBookingId;
    private String pickupAddress;

    private Double pickupLatitude;
    private Double pickupLongitude;

    private List<CreatePickupBillItemRequest> items;
}