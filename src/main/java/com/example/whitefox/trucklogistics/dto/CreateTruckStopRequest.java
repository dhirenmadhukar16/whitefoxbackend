package com.example.whitefox.trucklogistics.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class CreateTruckStopRequest {

    private UUID tripId;

    private UUID storeId;

    private String stopType;

    private Integer stopSequence;

    private Double latitude;

    private Double longitude;

    private LocalDateTime expectedArrivalTime;
}