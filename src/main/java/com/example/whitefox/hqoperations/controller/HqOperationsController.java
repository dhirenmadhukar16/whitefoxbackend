package com.example.whitefox.hqoperations.controller;

import com.example.whitefox.hqoperations.dto.AttachHqOutingQrRequest;
import com.example.whitefox.hqoperations.dto.HqGarmentResponse;
import com.example.whitefox.hqoperations.dto.HqLiveOperationsResponse;
import com.example.whitefox.hqoperations.dto.HqStoreDispatchGroupResponse;
import com.example.whitefox.hqoperations.service.HqOperationsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/hq")
@RequiredArgsConstructor
public class HqOperationsController {

    private final HqOperationsService hqOperationsService;

    @PatchMapping("/garments/{garmentId}/remove-old-qr")
    public HqGarmentResponse removeOldQr(
            @PathVariable UUID garmentId) {

        return hqOperationsService.removeOldQr(garmentId);
    }

    @PatchMapping("/garments/{garmentId}/start-cleaning")
    public HqGarmentResponse startCleaning(
            @PathVariable UUID garmentId) {

        return hqOperationsService.startCleaning(garmentId);
    }

    @PatchMapping("/garments/{garmentId}/mark-delayed")
    public HqGarmentResponse markDelayed(
            @PathVariable UUID garmentId) {

        return hqOperationsService.markDelayed(garmentId);
    }

    @PatchMapping("/garments/{garmentId}/complete-cleaning")
    public HqGarmentResponse completeCleaning(
            @PathVariable UUID garmentId) {

        return hqOperationsService.completeCleaning(garmentId);
    }

    @PatchMapping("/garments/{garmentId}/attach-outing-qr")
    public HqGarmentResponse attachOutingQr(
            @PathVariable UUID garmentId,
            @RequestBody AttachHqOutingQrRequest request) {

        return hqOperationsService.attachOutingQr(
                garmentId,
                request
        );
    }

    @PatchMapping("/garments/{garmentId}/ready-for-dispatch")
    public HqGarmentResponse readyForDispatch(
            @PathVariable UUID garmentId) {

        return hqOperationsService.readyForDispatch(garmentId);
    }

    @GetMapping("/garments/delayed")
    public List<HqGarmentResponse> getDelayedGarments() {

        return hqOperationsService.getDelayedGarments();
    }

    @GetMapping("/stores/{storeId}/pending-dispatch")
    public List<HqGarmentResponse> getPendingDispatch(
            @PathVariable UUID storeId) {

        return hqOperationsService.getPendingDispatchByStore(storeId);
    }

    @GetMapping("/dispatch/today")
    public List<HqGarmentResponse> todayDispatch() {

        return hqOperationsService.getTodayDispatch();
    }

    @GetMapping("/dispatch/yesterday")
    public List<HqGarmentResponse> yesterdayDispatch() {

        return hqOperationsService.getYesterdayDispatch();
    }

    @GetMapping("/dispatch/tomorrow")
    public List<HqGarmentResponse> tomorrowDispatch() {

        return hqOperationsService.getTomorrowDispatch();
    }
    @GetMapping("/live-operations")
    public HqLiveOperationsResponse liveOperations() {
        return hqOperationsService.getLiveOperations();
    }
    @GetMapping("/dispatch/ready-by-store")
    public List<HqStoreDispatchGroupResponse> readyDispatchByStore() {
        return hqOperationsService.getReadyDispatchGroupedByStore();
    }
}