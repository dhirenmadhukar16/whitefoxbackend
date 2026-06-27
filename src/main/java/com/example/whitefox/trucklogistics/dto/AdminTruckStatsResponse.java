package com.example.whitefox.adminpanel.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdminTruckStatsResponse {

    private Long totalTrucks;
    private Long availableTrucks;
    private Long onTripTrucks;
    private Long maintenanceTrucks;
    private Long offlineTrucks;

    private Long totalTrips;
    private Long activeTrips;
    private Long completedTrips;

    private Long totalManifests;
}