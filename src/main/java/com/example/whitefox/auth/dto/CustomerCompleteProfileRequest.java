package com.example.whitefox.auth.dto;

import lombok.Data;

@Data
public class CustomerCompleteProfileRequest {
    private String name;
    private String email;
    private String password;
    private String whatsappNumber;
    private String address;
}
