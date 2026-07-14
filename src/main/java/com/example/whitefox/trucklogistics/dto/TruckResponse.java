package com.example.whitefox.trucklogistics.dto;

import com.example.whitefox.trucklogistics.enums.TruckStatus;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class TruckResponse {

    private UUID id;

    private String truckNumber;

    private String driverName;

    private String driverPhone;
    
    private String email;

    private Double capacityKg;

    private Double currentLatitude;

    private Double currentLongitude;

    private TruckStatus status;

    private Boolean active;
}