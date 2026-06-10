package com.example.whitefox.pickupbill.dto;



import lombok.Data;
import java.util.List;
import java.util.UUID;

@Data
public class CreatePickupBillRequest {

    private UUID customerId;
    private UUID riderId;
    private UUID storeId;

    private String pickupAddress;

    private List<CreatePickupBillItemRequest> items;
}