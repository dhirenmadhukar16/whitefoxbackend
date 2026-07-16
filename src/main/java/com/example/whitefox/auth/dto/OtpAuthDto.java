package com.example.whitefox.auth.dto;

import lombok.Data;
import java.util.UUID;

public class OtpAuthDto {

    @Data
    public static class SendOtpRequest {
        private String phone;
    }

    @Data
    public static class VerifyCustomerOtpRequest {
        private String phone;
        private String otp;
    }

    @Data
    public static class VerifyRiderOtpRequest {
        private String phone;
        private String otp;
        private UUID storeId; // Required for new rider signup
        private String name;  // Optional, but good for signup
    }
}
