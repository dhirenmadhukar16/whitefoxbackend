package com.example.whitefox.garments.dto;


import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class ServiceCatalogResponse {

    private UUID id;

    private UUID categoryId;

    private String categoryName;

    private String serviceType; // Alias for categoryName for legacy clients

    private String itemName;

    private Double price;
    
    private String thumbnailUrl;

    private Boolean active;
}