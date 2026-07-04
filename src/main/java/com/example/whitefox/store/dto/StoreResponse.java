package com.example.whitefox.store.dto;



import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class StoreResponse {

    private UUID id;

    private String storeCode;

    private String name;

    private String phone;

    private String email;

    private String address;

    private String city;

    private Boolean active;
    private Double latitude;

    private Double longitude;

    private String generatedPassword;
}