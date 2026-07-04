package com.example.whitefox.trucklogistics.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class TruckLocationUpdateRequest {

    private UUID truckId;

    private UUID tripId;

    private Double latitude;

    private Double longitude;

    private Double speed;

    private Double heading;
}