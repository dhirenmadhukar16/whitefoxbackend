package com.example.whitefox.hqoperations.dto;

import com.example.whitefox.storeops.dto.StoreOrderSummaryResponse;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HqLiveOperationsResponse {

    private Long totalGarments;
    private Long delayedGarments;
    private Long cleaningInProgress;
    private Long readyForDispatch;

    private Long totalOrders;
    private Long activeOrders;

    private List<HqGarmentResponse> delayed;
    private List<HqGarmentResponse> todayDispatch;
    private List<HqGarmentResponse> readyDispatch;
    private List<StoreOrderSummaryResponse> latestOrders;
}