package com.example.whitefox.trucklogistics.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class TruckStopResponse {

    private UUID id;

    private UUID tripId;

    private UUID storeId;

    private String storeName;

    private String stopType;

    private Integer stopSequence;

    private Double latitude;

    private Double longitude;

    private LocalDateTime expectedArrivalTime;

    private LocalDateTime actualArrivalTime;

    private LocalDateTime departureTime;

    private Integer garmentsLoaded;

    private Integer garmentsUnloaded;

    private Double weightLoaded;

    private Double weightUnloaded;

    private String status;
}