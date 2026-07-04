package com.example.whitefox.DeliveryOtp.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class GenerateOtpResponse {

    private String otp;

    private String message;

    private LocalDateTime expiresAt;
}