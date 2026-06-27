package com.example.whitefox.trucklogistics.dto;

import lombok.Data;

@Data
public class CreateTruckRequest {

    private String truckNumber;

    private String driverName;

    private String driverPhone;

    private Double capacityKg;
}