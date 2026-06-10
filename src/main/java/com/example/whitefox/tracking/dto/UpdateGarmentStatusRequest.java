package com.example.whitefox.tracking.dto;



import com.example.whitefox.tracking.enums.GarmentStatus;
import lombok.Data;

@Data
public class UpdateGarmentStatusRequest {

    private GarmentStatus status;

    private String remarks;
}
