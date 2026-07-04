package com.example.whitefox.riders.dto;

import lombok.Data;

@Data
public class CreateRiderRequest {

    private String name;
    private String phone;
    private String email;
    private String vehicleNumber;
    private Double latitude;
    private Double longitude;
    private String password;
}
