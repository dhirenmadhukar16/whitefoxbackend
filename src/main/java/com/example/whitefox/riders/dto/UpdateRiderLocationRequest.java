package com.example.whitefox.riders.dto;

import lombok.Data;

@Data
public class UpdateRiderLocationRequest {
    private Double latitude;
    private Double longitude;
    private Double speed;
    private Double heading;
}