package com.example.whitefox.auth.controller;

import com.example.whitefox.auth.dto.LoginResponse;
import com.example.whitefox.auth.dto.OtpAuthDto;
import com.example.whitefox.auth.entity.User;
import com.example.whitefox.auth.repository.UserRepository;
import com.example.whitefox.auth.service.JwtService;
import com.example.whitefox.auth.service.OtpService;
import com.example.whitefox.storeemployee.entity.StoreEmployee;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/auth/store")
@RequiredArgsConstructor
public class StoreAuthController {

    private final OtpService otpService;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    @PostMapping("/send-otp")
    public void sendOtp(@RequestBody OtpAuthDto.SendOtpRequest request) {
        Optional<User> userOpt = userRepository.findByPhone(request.getPhone());
        if (userOpt.isEmpty() || (!"STORE_MANAGER".equals(userOpt.get().getRole()) && !"STORE_EMPLOYEE".equals(userOpt.get().getRole()))) {
            throw new RuntimeException("Store account not found. Please contact admin to create an account.");
        }
        otpService.sendOtp(request.getPhone(), "STORE");
    }

    @PostMapping("/verify-otp")
    public LoginResponse verifyOtp(@RequestBody OtpAuthDto.VerifyCustomerOtpRequest request) {
        boolean isValid = otpService.verifyOtp(request.getPhone(), "STORE", request.getOtp());
        if (!isValid) {
            throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.BAD_REQUEST, "Invalid or expired OTP");
        }

        User user = userRepository.findByPhone(request.getPhone())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!"STORE_MANAGER".equals(user.getRole()) && !"STORE_EMPLOYEE".equals(user.getRole())) {
             throw new RuntimeException("Not a valid store account");
        }

        String token = jwtService.generateToken(user.getEmail(), user.getRole());

        // Find store id if needed (optional)
        String storeId = null;
        if (user.getStoreId() != null) {
            storeId = user.getStoreId().toString();
        }

        return LoginResponse.builder()
                .token(token)
                .email(user.getEmail())
                .role(user.getRole())
                .userId(user.getId())
                .storeId(user.getStoreId())
                .build();
    }
}
