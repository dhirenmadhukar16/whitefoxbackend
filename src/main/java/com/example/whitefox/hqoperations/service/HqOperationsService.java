package com.example.whitefox.hqoperations.service;

import com.example.whitefox.hqoperations.dto.AttachHqOutingQrRequest;
import com.example.whitefox.hqoperations.dto.HqGarmentResponse;
import com.example.whitefox.hqoperations.dto.HqLiveOperationsResponse;
import com.example.whitefox.hqoperations.dto.HqStoreDispatchGroupResponse;

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

    List<HqGarmentResponse> getPendingDispatchByStore(UUID storeId);

    List<HqGarmentResponse> getTodayDispatch();

    List<HqGarmentResponse> getYesterdayDispatch();

    List<HqGarmentResponse> getTomorrowDispatch();
    HqLiveOperationsResponse getLiveOperations();
    List<HqStoreDispatchGroupResponse> getReadyDispatchGroupedByStore();
}