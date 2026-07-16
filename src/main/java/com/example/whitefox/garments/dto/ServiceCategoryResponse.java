package com.example.whitefox.garments.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class ServiceCategoryResponse {
    private UUID id;
    private String name;
    private Boolean active;
}
