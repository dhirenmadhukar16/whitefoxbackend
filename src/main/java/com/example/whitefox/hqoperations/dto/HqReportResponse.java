package com.example.whitefox.hqoperations.dto;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class HqReportResponse {
    private long comingToday;
    private long pendingGarments;
    private long dispatchedToday;
    private long totalRealTime;

    private List<StoreWiseOrder> storeWiseOrders;
    private List<CustomerWiseOrder> customerWiseOrders;

    @Data
    @Builder
    public static class StoreWiseOrder {
        private String storeName;
        private long orderCount;
    }

    @Data
    @Builder
    public static class CustomerWiseOrder {
        private String customerName;
        private String customerPhone;
        private long orderCount;
    }
}
