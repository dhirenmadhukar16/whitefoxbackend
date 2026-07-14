package com.example.whitefox.tracking.dto;

import lombok.Data;
import java.util.List;

@Data
public class GarmentDetailsDto {
    private String itemName;
    private String serviceType;
    private String color;
    private String stains;
    private String specialInstructions;
    private List<String> photoUrls;
}
