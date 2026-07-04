package com.example.whitefox.riders.controller;


import com.example.whitefox.riders.dto.*;
import com.example.whitefox.riders.service.RiderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/riders")
@RequiredArgsConstructor
public class RiderController {

    private final RiderService riderService;

    @PostMapping
    public RiderResponse createRider(@RequestBody CreateRiderRequest request) {
        return riderService.createRider(request);
    }

    @GetMapping
    public List<RiderResponse> getAllRiders() {
        return riderService.getAllRiders();
    }

    @GetMapping("/{id}")
    public RiderResponse getRider(@PathVariable UUID id) {
        return riderService.getRider(id);
    }

    @PutMapping("/{id}")
    public RiderResponse updateRider(
            @PathVariable UUID id,
            @RequestBody CreateRiderRequest request
    ) {
        return riderService.updateRider(id, request);
    }

    @PatchMapping("/{id}/status")
    public RiderResponse updateStatus(
            @PathVariable UUID id,
            @RequestBody UpdateRiderStatusRequest request
    ) {
        return riderService.updateStatus(id, request);
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivateRider(@PathVariable UUID id) {
        riderService.deactivateRider(id);
        return ResponseEntity.noContent().build();
    }
    @PatchMapping("/riders/{riderId}/location")
    public RiderResponse updateLocation(
            @PathVariable UUID riderId,
            @RequestBody UpdateRiderLocationRequest request
    ) {
        return riderService.updateLocation(riderId, request);
    }
}
