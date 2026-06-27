package com.example.whitefox.tracking.controller;



import com.example.whitefox.tracking.dto.*;
import com.example.whitefox.tracking.service.GarmentTrackingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/tracking")
@RequiredArgsConstructor
public class GarmentTrackingController {

    private final GarmentTrackingService garmentTrackingService;

    @PostMapping("/orders/{orderId}/garments")
    public GarmentResponse createGarment(
            @PathVariable UUID orderId,
            @RequestBody CreateGarmentRequest request
    ) {
        return garmentTrackingService.createGarment(
                orderId,
                request
        );
    }

    @GetMapping("/orders/{orderId}/garments")
    public List<GarmentResponse> getGarmentsByOrder(
            @PathVariable UUID orderId
    ) {
        return garmentTrackingService.getGarmentsByOrder(orderId);
    }

    @PatchMapping("/garments/{garmentId}/status")
    public GarmentResponse updateStatus(
            @PathVariable UUID garmentId,
            @RequestBody UpdateGarmentStatusRequest request
    ) {
        return garmentTrackingService.updateStatus(
                garmentId,
                request
        );
    }

    @PatchMapping("/garments/{garmentId}/outing-qr")
    public GarmentResponse attachOutingQr(
            @PathVariable UUID garmentId,
            @RequestBody AttachOutingQrRequest request
    ) {
        return garmentTrackingService.attachOutingQr(
                garmentId,
                request
        );
    }
    @PostMapping("/orders/{orderId}/generate-garments")
    public List<GarmentResponse> generateGarmentsFromOrder(
            @PathVariable UUID orderId
    ) {
        return garmentTrackingService.generateGarmentsFromOrder(orderId);
    }
    @GetMapping("/scan/{qrCode}")
    public GarmentResponse scanQr(
            @PathVariable String qrCode
    ) {
        return garmentTrackingService.scanQr(qrCode);
    }
    @PatchMapping("/orders/{orderId}/send-to-hq")
    public List<GarmentResponse> sendOrderGarmentsToHq(
            @PathVariable UUID orderId
    ) {
        return garmentTrackingService.sendOrderGarmentsToHq(orderId);
    }
    @PatchMapping("/orders/{orderId}/receive-at-hq")
    public List<GarmentResponse> receiveOrderGarmentsAtHq(
            @PathVariable UUID orderId
    ) {
        return garmentTrackingService.receiveOrderGarmentsAtHq(orderId);
    }
    @PatchMapping("/orders/{orderId}/send-back-to-store")
    public List<GarmentResponse> sendOrderGarmentsBackToStore(
            @PathVariable UUID orderId
    ) {
        return garmentTrackingService.sendOrderGarmentsBackToStore(orderId);
    }
    @PatchMapping("/orders/{orderId}/receive-back-at-store")
    public List<GarmentResponse> receiveOrderGarmentsBackAtStore(
            @PathVariable UUID orderId
    ) {
        return garmentTrackingService.receiveOrderGarmentsBackAtStore(orderId);
    }
}
