package com.example.whitefox.auth.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class LoginResponse {

    private String token;

    private String role;

    private String email;

    private UUID storeId;

    private UUID userId;
}
