
package com.example.whitefox.auth.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String identifier; // Can be email or phone number
    private String email;
    private String phone;
    private String password;

    public String getIdentifier() {
        if (identifier != null && !identifier.isEmpty()) return identifier;
        if (email != null && !email.isEmpty()) return email;
        if (phone != null && !phone.isEmpty()) return phone;
        return null;
    }
}
