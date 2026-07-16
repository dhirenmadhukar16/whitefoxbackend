package com.example.whitefox.riders.controller;

import com.example.whitefox.riders.dto.RiderResponse;
import com.example.whitefox.riders.service.RiderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/riders")
@RequiredArgsConstructor
public class RiderAppController {

    private final RiderService riderService;

    @GetMapping("/me")
    public RiderResponse getMe() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName(); // The username is the email
        return riderService.getMe(email);
    }
    
    @org.springframework.web.bind.annotation.PutMapping("/me")
    public RiderResponse updateMe(@org.springframework.web.bind.annotation.RequestBody com.example.whitefox.riders.dto.CompleteRiderProfileRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return riderService.updateMe(email, request);
    }
}
