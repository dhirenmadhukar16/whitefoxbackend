package com.example.whitefox.trucklogistics.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class CreateManifestRequest {

    private UUID tripId;

    private UUID sourceStoreId;

    private UUID destinationStoreId;

    private String movementType;
}