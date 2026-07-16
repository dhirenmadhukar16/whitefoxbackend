package com.example.whitefox.auth.controller;

import com.example.whitefox.auth.dto.AuthCheckDto;
import com.example.whitefox.auth.dto.LoginRequest;
import com.example.whitefox.auth.dto.LoginResponse;
import com.example.whitefox.auth.dto.RegisterRequest;
import com.example.whitefox.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/check")
    public AuthCheckDto.Response checkUser(@RequestBody AuthCheckDto.Request request) {
        return authService.checkUser(request);
    }

    @PostMapping("/login")
    public LoginResponse login(
            @RequestBody LoginRequest request
    ) {
        return authService.login(request);
    }

    @PostMapping("/register")
    public LoginResponse register(
            @RequestBody RegisterRequest request
    ) {
        return authService.register(request);
    }
}