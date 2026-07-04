package com.example.whitefox.pickupbill.dto;



import lombok.Data;

@Data
public class CreatePickupBillItemRequest {

    private String itemName;

    private String serviceType;

    private Integer quantity;

    private Double unitPrice;

    private String conditionNote;

    private String photoUrl;
}


