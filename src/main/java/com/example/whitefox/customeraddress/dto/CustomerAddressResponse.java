package com.example.whitefox.customeraddress.dto;



import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class CustomerAddressResponse {

    private UUID id;

    private UUID customerId;

    private String customerName;

    private String label;

    private String addressLine;

    private String landmark;

    private String city;

    private String state;

    private String pincode;

    private Double latitude;

    private Double longitude;

    private Boolean defaultAddress;
}
