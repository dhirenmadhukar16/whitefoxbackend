package com.example.whitefox.garments.dto;


import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class ServiceCatalogResponse {

    private UUID id;

    private String serviceType;

    private String itemName;

    private Double price;

    private Boolean active;
}