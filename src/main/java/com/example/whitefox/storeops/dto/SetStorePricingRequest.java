package com.example.whitefox.storeops.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class SetStorePricingRequest {
    private UUID serviceId;
    private Double customPrice;
}
