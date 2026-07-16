package com.example.whitefox.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class AuthCheckDto {

    @Data
    public static class Request {
        private String identifier; // email or phone
        private String email;
        private String phone;

        public String getIdentifier() {
            if (identifier != null && !identifier.isEmpty()) return identifier;
            if (email != null && !email.isEmpty()) return email;
            if (phone != null && !phone.isEmpty()) return phone;
            return null;
        }
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response {
        private boolean exists;
        private String authMethod; // "PASSWORD" or "OTP"
        private String role;
        private boolean active;
    }
}
