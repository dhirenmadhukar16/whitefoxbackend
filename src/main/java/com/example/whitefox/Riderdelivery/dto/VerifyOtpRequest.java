package com.example.whitefox.Riderdelivery.dto;

import lombok.Data;

@Data
public class VerifyOtpRequest {
    private String otp;
    private boolean cashCollected;
}
