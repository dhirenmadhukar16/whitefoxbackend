package com.example.whitefox.trucklogistics.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class TruckLiveLocationResponse {

    private UUID truckId;

    private String truckNumber;

    private String driverName;

    private String driverPhone;

    private UUID activeTripId;

    private String tripNumber;

    private Double latitude;

    private Double longitude;

    private String status;
}