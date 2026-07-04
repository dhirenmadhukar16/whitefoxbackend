package com.example.whitefox.storeops.dto;

import lombok.Builder;
import lombok.Data;
import java.util.UUID;

@Data
@Builder
public class StoreServicePricingDto {
    private UUID serviceId;
    private String serviceType;
    private String itemName;
    private Double globalPrice;
    private Double storeCustomPrice;
    private Boolean isOverridden;
}
