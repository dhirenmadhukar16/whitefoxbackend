package com.example.whitefox.customerbooking.dto;



import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class CustomerBookingItemResponse {

    private UUID catalogId;

    private String serviceType;

    private String itemName;

    private Integer quantity;

    private Double unitPrice;

    private Double estimatedPrice;
}
