package com.example.whitefox.auth.service;

import com.example.whitefox.auth.dto.LoginRequest;
import com.example.whitefox.auth.dto.LoginResponse;
import com.example.whitefox.auth.dto.RegisterRequest;
import com.example.whitefox.auth.entity.User;
import com.example.whitefox.auth.repository.UserRepository;
import com.example.whitefox.common.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public LoginResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(
                request.getEmail()
        ).orElseThrow(() ->
                new ResourceNotFoundException(
                        "Invalid credentials"
                )
        );

        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword()
        )) {
            throw new RuntimeException("Invalid credentials");
        }
        String token = jwtService.generateToken(
                user.getEmail(),
                user.getRole()
        );


        return LoginResponse.builder()
                .token(token)
                .email(user.getEmail())
                .role(user.getRole())
                .storeId(user.getStoreId())
                .userId(user.getId())
                .build();
    }

    public LoginResponse register(
            RegisterRequest request
    ) {

        if (userRepository.findByEmail(
                request.getEmail()
        ).isPresent()) {

            throw new RuntimeException(
                    "Email already exists"
            );
        }

        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .password(
                        passwordEncoder.encode(
                                request.getPassword()
                        )
                )
                .role("USER")
                .active(true)
                .build();

        userRepository.save(user);
        String token = jwtService.generateToken(
                user.getEmail(),
                user.getRole()
        );


        return LoginResponse.builder()
                .token(token)
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }
}