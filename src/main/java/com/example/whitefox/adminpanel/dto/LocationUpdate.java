package com.example.whitefox.adminpanel.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocationUpdate {
    private String entityId;    // riderId or truckId
    private String entityType;  // "RIDER" or "TRUCK"
    private double latitude;
    private double longitude;
    private double heading;     // for rotating the vehicle icon
    private Instant timestamp;
}
