package com.example.whitefox.trucklogistics.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class ManifestItemResponse {

    private UUID id;

    private UUID manifestId;

    private UUID bagId;

    private String bagQrCode;

    private int totalGarmentsInBag;

    private String qrCode;

    private UUID destinationStoreId;

    private String destinationStoreName;

    private String status;

    private LocalDateTime loadedAt;

    private LocalDateTime deliveredAt;
}