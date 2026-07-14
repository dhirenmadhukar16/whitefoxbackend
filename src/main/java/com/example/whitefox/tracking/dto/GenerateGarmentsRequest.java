package com.example.whitefox.tracking.dto;

import lombok.Data;
import java.util.List;

@Data
public class GenerateGarmentsRequest {
    private List<GarmentDetailsDto> garments;
}
