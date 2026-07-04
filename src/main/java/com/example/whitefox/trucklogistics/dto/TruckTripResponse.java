package com.example.whitefox.trucklogistics.dto;

import com.example.whitefox.trucklogistics.enums.TruckTripStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class TruckTripResponse {

    private UUID id;

    private UUID truckId;

    private String truckNumber;

    private String tripNumber;

    private String shift;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private TruckTripStatus status;

    private Integer totalStores;

    private Integer completedStores;

    private Integer totalGarments;

    private Double totalWeight;

    private Boolean returnTrip;
}