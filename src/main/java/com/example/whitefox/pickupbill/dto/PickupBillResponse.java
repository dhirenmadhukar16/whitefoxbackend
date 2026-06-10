package com.example.whitefox.pickupbill.dto;



import com.example.whitefox.pickupbill.enums.PickupBillStatus;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class PickupBillResponse {

    private UUID id;

    private String billNumber;

    private UUID customerId;

    private String customerName;

    private UUID riderId;

    private String riderName;

    private UUID storeId;

    private String storeName;

    private String pickupAddress;

    private Double subtotal;

    private Double gst;

    private Double totalAmount;

    private PickupBillStatus status;

    private List<PickupBillItemResponse> items;
}
