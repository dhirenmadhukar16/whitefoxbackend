package com.example.whitefox.auth.controller;

import com.example.whitefox.auth.dto.LoginResponse;
import com.example.whitefox.auth.dto.OtpAuthDto;
import com.example.whitefox.auth.entity.User;
import com.example.whitefox.auth.repository.UserRepository;
import com.example.whitefox.auth.service.JwtService;
import com.example.whitefox.auth.service.OtpService;
import com.example.whitefox.riders.entity.Rider;
import com.example.whitefox.riders.enums.RiderStatus;
import com.example.whitefox.riders.repository.RiderRepository;
import com.example.whitefox.store.entity.Store;
import com.example.whitefox.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/auth/rider")
@RequiredArgsConstructor
public class RiderAuthController {

    private final OtpService otpService;
    private final RiderRepository riderRepository;
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/send-otp")
    public void sendOtp(@RequestBody OtpAuthDto.SendOtpRequest request) {
        otpService.sendOtp(request.getPhone(), "RIDER");
    }

    @PostMapping("/verify-otp")
    public LoginResponse verifyOtp(@RequestBody OtpAuthDto.VerifyRiderOtpRequest request) {
        boolean isValid = otpService.verifyOtp(request.getPhone(), "RIDER", request.getOtp());
        if (!isValid) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid or expired OTP");
        }

        Optional<Rider> riderOpt = riderRepository.findByPhone(request.getPhone());
        Rider rider;
        User user;

        if (riderOpt.isEmpty()) {
            rider = Rider.builder()
                    .name("New Rider")
                    .phone(request.getPhone())
                    .email("rider_" + request.getPhone() + "@whitefox.local")
                    .store(null)
                    .build();
            
            rider = riderRepository.save(rider);
            rider.setStatus(RiderStatus.PENDING_APPROVAL);
            rider = riderRepository.save(rider);

            Optional<User> userOpt = userRepository.findByPhone(request.getPhone());
            if (userOpt.isEmpty()) {
                user = User.builder()
                        .firstName("Rider")
                        .lastName(request.getPhone())
                        .phone(request.getPhone())
                        .email("rider_" + request.getPhone() + "@whitefox.local")
                        .password(passwordEncoder.encode(UUID.randomUUID().toString()))
                        .role("RIDER")
                        .storeId(null)
                        .active(true)
                        .build();
                user = userRepository.save(user);
            } else {
                user = userOpt.get();
            }
        } else {
            rider = riderOpt.get();
            Optional<User> existingUserOpt = userRepository.findByPhone(request.getPhone());
            if (existingUserOpt.isEmpty()) {
                user = User.builder()
                        .firstName(rider.getName())
                        .lastName("Rider")
                        .phone(request.getPhone())
                        .email(rider.getEmail() != null ? rider.getEmail() : "rider_" + request.getPhone() + "@whitefox.local")
                        .password(passwordEncoder.encode(UUID.randomUUID().toString()))
                        .role("RIDER")
                        .storeId(rider.getStore() != null ? rider.getStore().getId() : null)
                        .active(true)
                        .build();
                user = userRepository.save(user);
            } else {
                user = existingUserOpt.get();
            }
        }

        String token = jwtService.generateToken(user.getEmail(), user.getRole());

        return LoginResponse.builder()
                .token(token)
                .email(user.getEmail())
                .role(user.getRole())
                .userId(rider.getId())
                .storeId(user.getStoreId())
                .build();
    }
}
