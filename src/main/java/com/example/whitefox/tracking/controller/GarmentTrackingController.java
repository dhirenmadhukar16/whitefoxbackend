package com.example.whitefox.tracking.controller;



import com.example.whitefox.tracking.dto.*;
import com.example.whitefox.tracking.service.GarmentTrackingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

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

    @PostMapping("/garments/{garmentId}/report-missing")
    public GarmentResponse reportGarmentMissing(
            @PathVariable UUID garmentId
    ) {
        return garmentTrackingService.reportGarmentMissing(garmentId);
    }

    @PostMapping("/orders/{orderId}/generate-garments")
    public List<GarmentResponse> generateGarmentsFromOrder(
            @PathVariable UUID orderId,
            @RequestBody com.example.whitefox.tracking.dto.GenerateGarmentsRequest request
    ) {
        return garmentTrackingService.generateGarmentsFromOrder(orderId, request);
    }
    @GetMapping("/scan/{qrCode}")
    public GarmentResponse scanQr(
            @PathVariable String qrCode
    ) {
        return garmentTrackingService.scanQr(qrCode);
    }
    @PatchMapping("/scan-action/{qrCode}")
    public GarmentResponse scanAction(
            @PathVariable String qrCode,
            @RequestParam String role
    ) {
        return garmentTrackingService.scanAction(qrCode, role);
    }

    @GetMapping("/garments")
    public List<GarmentResponse> getGarmentsByStatus(
            @RequestParam(required = false) String status
    ) {
        if (status != null && !status.isEmpty()) {
            return garmentTrackingService.getGarmentsByStatus(status);
        }
        return garmentTrackingService.getAllHqGarments();
    }

    @GetMapping("/hq/garments")
    public List<GarmentResponse> getAllHqGarments() {
        return garmentTrackingService.getAllHqGarments();
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

    @GetMapping("/truck/pickup-routes")
    public List<TruckRouteResponse> getTruckPickupRoutes() {
        return garmentTrackingService.getTruckPickupRoutes();
    }

    @GetMapping("/truck/drop-routes")
    public List<TruckRouteResponse> getTruckDropRoutes() {
        return garmentTrackingService.getTruckDropRoutes();
    }

    @GetMapping("/truck/history")
    public List<TrackingHistoryResponse> getTruckHistory(
            @RequestParam(required = false) String date,
            @RequestParam String role
    ) {
        return garmentTrackingService.getTruckHistory(date, role);
    }

    @PatchMapping("/store/{storeId}/approve-drops")
    public List<GarmentResponse> approveStoreDrops(
            @PathVariable UUID storeId
    ) {
        return garmentTrackingService.approveStoreDrops(storeId);
    }

    @PostMapping("/truck/pickup-store/{storeId}")
    public ResponseEntity<List<GarmentResponse>> pickupStore(@PathVariable UUID storeId) {
        return ResponseEntity.ok(garmentTrackingService.pickupStore(storeId));
    }

    @PostMapping("/truck/drop-hq")
    public ResponseEntity<List<GarmentResponse>> dropHq() {
        return ResponseEntity.ok(garmentTrackingService.dropHq());
    }

    @PostMapping("/hq/approve-drop")
    public ResponseEntity<List<GarmentResponse>> approveDrop() {
        return ResponseEntity.ok(garmentTrackingService.approveDrop());
    }

    @GetMapping("/hq/dispatched-history")
    public ResponseEntity<List<GarmentResponse>> getDispatchedHistory(
            @RequestParam(required = false) String date
    ) {
        return ResponseEntity.ok(garmentTrackingService.getDispatchedHistory(date));
    }
}
