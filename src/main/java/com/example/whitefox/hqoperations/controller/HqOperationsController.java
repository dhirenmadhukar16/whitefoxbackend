package com.example.whitefox.hqoperations.controller;

import com.example.whitefox.hqoperations.dto.AttachHqOutingQrRequest;
import com.example.whitefox.hqoperations.dto.HqGarmentResponse;
import com.example.whitefox.hqoperations.dto.HqLiveOperationsResponse;
import com.example.whitefox.hqoperations.dto.HqReportResponse;
import com.example.whitefox.hqoperations.dto.HqStoreDispatchGroupResponse;
import com.example.whitefox.hqoperations.dto.HqStoreDashboardResponse;
import com.example.whitefox.hqoperations.dto.HqCustomerDashboardResponse;
import com.example.whitefox.hqoperations.service.HqOperationsService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/hq")
@RequiredArgsConstructor
public class HqOperationsController {

    private final HqOperationsService hqOperationsService;
    private final com.example.whitefox.tracking.service.BagService bagService;

    @PostMapping("/bags/receive")
    public com.example.whitefox.tracking.dto.BagResponse receiveBag(
            @RequestParam String qrCode) {
        
        com.example.whitefox.tracking.dto.BagResponse bag = bagService.getBagByQrCode(qrCode);
        return bagService.updateBagStatus(bag.getId(), "RECEIVED_AT_HQ");
    }

    @PostMapping("/bags/dispatch")
    public com.example.whitefox.tracking.dto.BagResponse dispatchBag(
            @RequestBody com.example.whitefox.tracking.dto.CreateBagRequest request) {
        
        return bagService.createBag(request);
    }

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

    @GetMapping("/reports")
    public HqReportResponse getReports(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        if (startDate == null) {
            startDate = LocalDate.now();
        }
        if (endDate == null) {
            endDate = LocalDate.now();
        }
        return hqOperationsService.getHqReport(startDate, endDate);
    }

    @GetMapping("/live-operations")
    public HqLiveOperationsResponse liveOperations() {
        return hqOperationsService.getLiveOperations();
    }

    @GetMapping("/dispatch/ready-by-store")
    public List<HqStoreDispatchGroupResponse> readyDispatchByStore() {
        return hqOperationsService.getReadyDispatchGroupedByStore();
    }

    @GetMapping("/dashboard/upcoming-garments")
    public List<HqGarmentResponse> upcomingGarments() {
        return hqOperationsService.getUpcomingGarments();
    }

    @GetMapping("/dashboard/in-hq-garments")
    public List<HqGarmentResponse> inHqGarments() {
        return hqOperationsService.getInHqGarments();
    }

    @GetMapping("/dashboard/store-wise")
    public List<HqStoreDashboardResponse> storeWiseDashboard() {
        return hqOperationsService.getStoreWiseDashboard();
    }

    @GetMapping("/dashboard/customer-wise")
    public List<HqCustomerDashboardResponse> customerWiseDashboard() {
        return hqOperationsService.getCustomerWiseDashboard();
    }
}