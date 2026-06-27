package com.example.whitefox.adminpanel.dto;

import com.example.whitefox.pickupbill.enums.PickupBillStatus;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class AdminPickupBillResponse {

    private UUID pickupBillId;
    private String billNumber;

    private UUID customerId;
    private String customerName;

    private UUID riderId;
    private String riderName;

    private UUID storeId;
    private String storeName;

    private UUID assignedStoreId;
    private String assignedStoreName;

    private UUID dropStoreId;
    private String dropStoreName;

    private String pickupAddress;

    private Double subtotal;
    private Double gst;
    private Double totalAmount;

    private PickupBillStatus status;
}