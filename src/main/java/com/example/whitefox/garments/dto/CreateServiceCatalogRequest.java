package com.example.whitefox.garments.dto;



import lombok.Data;
import java.util.UUID;

@Data
public class CreateServiceCatalogRequest {

    private UUID categoryId;
    private String serviceType; // Legacy fallback
    private String itemName;
    private Double price;
    private String thumbnailUrl;
}