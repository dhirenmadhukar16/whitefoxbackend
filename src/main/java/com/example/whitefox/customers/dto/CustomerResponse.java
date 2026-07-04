package com.example.whitefox.customers.dto;



import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class CustomerResponse {

    private UUID id;

    private String customerCode;

    private String name;

    private String phone;

    private String email;

    private String address;

    private String city;

    private Boolean active;
}