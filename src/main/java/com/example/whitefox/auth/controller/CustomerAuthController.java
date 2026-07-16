package com.example.whitefox.auth.controller;

import com.example.whitefox.auth.dto.LoginResponse;
import com.example.whitefox.auth.dto.OtpAuthDto;
import com.example.whitefox.auth.dto.CustomerCompleteProfileRequest;
import com.example.whitefox.auth.entity.User;
import com.example.whitefox.auth.repository.UserRepository;
import com.example.whitefox.auth.service.JwtService;
import com.example.whitefox.auth.service.OtpService;
import com.example.whitefox.customers.entity.Customer;
import com.example.whitefox.customers.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/auth/customer")
@RequiredArgsConstructor
public class CustomerAuthController {

    private final OtpService otpService;
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/send-otp")
    public void sendOtp(@RequestBody OtpAuthDto.SendOtpRequest request) {
        otpService.sendOtp(request.getPhone(), "CUSTOMER");
    }

    @PostMapping("/verify-otp")
    public LoginResponse verifyOtp(@RequestBody OtpAuthDto.VerifyCustomerOtpRequest request) {
        boolean isValid = otpService.verifyOtp(request.getPhone(), "CUSTOMER", request.getOtp());
        if (!isValid) {
            throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.BAD_REQUEST, "Invalid or expired OTP");
        }

        Optional<Customer> customerOpt = customerRepository.findByPhone(request.getPhone());
        Customer customer;
        User user;

        boolean isNew = false;
        if (customerOpt.isEmpty()) {
            isNew = true;
            // Register new
            customer = Customer.builder()
                    .customerCode("CUST-" + System.currentTimeMillis())
                    .name("New User") // They will update profile later
                    .phone(request.getPhone())
                    .build();
            customer = customerRepository.save(customer);

            // Also check if user exists by phone
            Optional<User> userOpt = userRepository.findByPhone(request.getPhone());
            if (userOpt.isEmpty()) {
                user = User.builder()
                        .firstName("New")
                        .lastName("User")
                        .phone(request.getPhone())
                        // email is required by User entity usually, but let's use a dummy if nullable=false
                        .email("pending_" + request.getPhone() + "@whitefox.local")
                        .password(passwordEncoder.encode(UUID.randomUUID().toString()))
                        .role("CUSTOMER")
                        .active(true)
                        .build();
                user = userRepository.save(user);
            } else {
                user = userOpt.get();
            }
        } else {
            customer = customerOpt.get();
            user = userRepository.findByPhone(request.getPhone()).orElseThrow();
        }

        String token = jwtService.generateToken(user.getEmail(), user.getRole());

        return LoginResponse.builder()
                .token(token)
                .email(user.getEmail())
                .role(user.getRole())
                .userId(customer.getId())
                .isNewUser(isNew)
                .build();
    }

    @PostMapping("/complete-profile")
    public void completeProfile(@RequestBody CustomerCompleteProfileRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        Customer customer = customerRepository.findByPhone(user.getPhone())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        if (request.getName() != null && !request.getName().isEmpty()) {
            customer.setName(request.getName());
            String[] parts = request.getName().split(" ", 2);
            user.setFirstName(parts[0]);
            if (parts.length > 1) {
                user.setLastName(parts[1]);
            } else {
                user.setLastName("");
            }
        }

        if (request.getEmail() != null && !request.getEmail().isEmpty()) {
            customer.setEmail(request.getEmail());
            user.setEmail(request.getEmail());
        }

        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        if (request.getWhatsappNumber() != null) {
            customer.setWhatsappNumber(request.getWhatsappNumber());
        }

        if (request.getAddress() != null) {
            customer.setAddress(request.getAddress());
        }

        userRepository.save(user);
        customerRepository.save(customer);
    }
}
