package com.example.whitefox.trucklogistics.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class CreateTruckTripRequest {

    private UUID truckId;

    private String shift;

    private Boolean returnTrip;
}