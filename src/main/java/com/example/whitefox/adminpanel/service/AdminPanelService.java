package com.example.whitefox.adminpanel.service;

import com.example.whitefox.adminpanel.dto.*;
import com.example.whitefox.adminpanel.dto.AdminReviewResponse;
import java.util.List;
import com.example.whitefox.adminpanel.dto.AdminReviewResponse;
import com.example.whitefox.adminpanel.dto.AdminBookingResponse;
import com.example.whitefox.adminpanel.dto.AdminCatalogStatsResponse;
import com.example.whitefox.adminpanel.dto.AdminHqOperationsResponse;
import com.example.whitefox.adminpanel.dto.AdminSearchResponse;
import com.example.whitefox.adminpanel.dto.AdminTruckStatsResponse;

import java.util.UUID;

public interface AdminPanelService {

    AdminDashboardResponse getDashboard();
    java.util.List<AdminStoreStatsResponse> getStoreStats();
    List<AdminRiderStatsResponse> getRiderStats();
    AdminRiderDetailResponse getRiderDetails(UUID id);
    void resetRiderPassword(UUID id, String newPassword);
    void assignRiderToOrder(UUID orderId, UUID riderId);
    void cancelOrder(UUID orderId);
    List<AdminOrderResponse> getAllOrdersForAdmin();
    List<AdminCustomerStatsResponse> getCustomerStats();
    List<AdminPickupBillResponse> getAllPickupBillsForAdmin();
    AdminGarmentStatsResponse getGarmentStats();
    List<AdminBookingResponse> getAllBookingsForAdmin();
    AdminFinanceStatsResponse getFinanceStats();
    List<AdminReviewResponse> getAllReviewsForAdmin();
    AdminCatalogStatsResponse getCatalogStats();
    List<AdminSearchResponse> search(String keyword);
    AdminHqOperationsResponse getHqOperations();
    AdminTruckStatsResponse getTruckStats();
}
