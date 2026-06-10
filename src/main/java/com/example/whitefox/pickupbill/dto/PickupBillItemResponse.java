package com.example.whitefox.pickupbill.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PickupBillItemResponse {

    private String itemName;

    private String serviceType;

    private Integer quantity;

    private Double unitPrice;

    private Double totalPrice;

    private String conditionNote;

    private String photoUrl;
}