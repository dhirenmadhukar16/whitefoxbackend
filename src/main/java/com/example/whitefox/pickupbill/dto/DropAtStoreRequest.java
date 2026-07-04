package com.example.whitefox.pickupbill.dto;

//package com.example.whitefox.pickupbill.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class DropAtStoreRequest {

    private UUID dropStoreId;

    private Double dropLatitude;

    private Double dropLongitude;
}
