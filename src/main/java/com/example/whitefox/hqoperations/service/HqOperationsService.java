package com.example.whitefox.hqoperations.service;

import com.example.whitefox.hqoperations.dto.AttachHqOutingQrRequest;
import com.example.whitefox.hqoperations.dto.HqGarmentResponse;
import com.example.whitefox.hqoperations.dto.HqLiveOperationsResponse;
import com.example.whitefox.hqoperations.dto.HqStoreDispatchGroupResponse;
import com.example.whitefox.hqoperations.dto.HqReportResponse;
import com.example.whitefox.hqoperations.dto.HqStoreDashboardResponse;
import com.example.whitefox.hqoperations.dto.HqCustomerDashboardResponse;

import java.time.LocalDate;

import java.util.List;
import java.util.UUID;

public interface HqOperationsService {

    HqGarmentResponse removeOldQr(UUID garmentId);

    HqGarmentResponse startCleaning(UUID garmentId);

    HqGarmentResponse markDelayed(UUID garmentId);

    HqGarmentResponse completeCleaning(UUID garmentId);

    HqGarmentResponse attachOutingQr(
            UUID garmentId,
            AttachHqOutingQrRequest request
    );

    HqGarmentResponse readyForDispatch(UUID garmentId);

    List<HqGarmentResponse> getDelayedGarments();

    List<HqGarmentResponse> getUpcomingGarments();

    List<HqGarmentResponse> getInHqGarments();

    List<HqStoreDashboardResponse> getStoreWiseDashboard();

    List<HqCustomerDashboardResponse> getCustomerWiseDashboard();

    List<HqGarmentResponse> getPendingDispatchByStore(UUID storeId);

    List<HqGarmentResponse> getTodayDispatch();

    List<HqGarmentResponse> getYesterdayDispatch();

    List<HqGarmentResponse> getTomorrowDispatch();
    HqLiveOperationsResponse getLiveOperations();
    List<HqStoreDispatchGroupResponse> getReadyDispatchGroupedByStore();
    
    HqReportResponse getHqReport(LocalDate startDate, LocalDate endDate);
}