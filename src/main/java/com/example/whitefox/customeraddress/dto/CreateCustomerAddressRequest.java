package com.example.whitefox.customeraddress.dto;



import lombok.Data;

import java.util.UUID;

@Data
public class CreateCustomerAddressRequest {

    private UUID customerId;

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
