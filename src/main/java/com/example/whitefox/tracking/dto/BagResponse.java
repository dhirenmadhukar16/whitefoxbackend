package com.example.whitefox.tracking.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class BagResponse {
    private UUID id;
    private String qrCode;
    private UUID sourceStoreId;
    private String sourceStoreName;
    private UUID destinationStoreId;
    private String destinationStoreName;
    private String status;
    private int totalGarments;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
