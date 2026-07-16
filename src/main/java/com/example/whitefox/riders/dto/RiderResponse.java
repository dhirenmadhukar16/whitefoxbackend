package com.example.whitefox.riders.dto;



import com.example.whitefox.riders.enums.RiderStatus;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class RiderResponse {

    private UUID id;
    private String riderCode;
    private String name;
    private String phone;
    private String email;
    private String vehicleNumber;
    private Double latitude;
    private Double longitude;
    private RiderStatus status;
    private Boolean active;
    private String whatsappNumber;
    private UUID storeId;
    private String generatedPassword;
}
