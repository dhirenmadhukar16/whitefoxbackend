package com.example.whitefox.trucklogistics.dto;

import com.example.whitefox.trucklogistics.enums.TruckManifestStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class ManifestResponse {

    private UUID id;

    private UUID tripId;

    private String tripNumber;

    private UUID sourceStoreId;

    private String sourceStoreName;

    private UUID destinationStoreId;

    private String destinationStoreName;

    private String movementType;

    private String manifestNumber;

    private Integer totalGarments;

    private Double totalWeight;

    private TruckManifestStatus status;

    private LocalDateTime loadedAt;

    private LocalDateTime deliveredAt;
}