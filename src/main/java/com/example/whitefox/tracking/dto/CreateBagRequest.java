package com.example.whitefox.tracking.dto;

import lombok.Data;
import java.util.List;
import java.util.UUID;

@Data
public class CreateBagRequest {
    private String qrCode;
    private UUID sourceStoreId;
    private UUID destinationStoreId;
    private List<UUID> garmentIds;
}
