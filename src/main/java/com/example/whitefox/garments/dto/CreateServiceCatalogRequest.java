package com.example.whitefox.garments.dto;



import lombok.Data;

@Data
public class CreateServiceCatalogRequest {

    private String serviceType;
    private String itemName;
    private Double price;
}