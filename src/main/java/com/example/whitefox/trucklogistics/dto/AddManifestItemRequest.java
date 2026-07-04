package com.example.whitefox.trucklogistics.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class AddManifestItemRequest {

    private UUID garmentId;
    private UUID destinationStoreId;
    private String qrCode;
}