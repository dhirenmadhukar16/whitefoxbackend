package com.example.whitefox.customers.dto;

//package com.example.whitefox.customer.dto;

import lombok.Data;

@Data
public class CreateCustomerRequest {

    private String name;

    private String phone;

    private String email;

    private String address;

    private String city;
}