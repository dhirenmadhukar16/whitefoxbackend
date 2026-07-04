package com.example.whitefox.adminpanel.controller;


import com.example.whitefox.adminpanel.dto.AdminReviewResponse;
import com.example.whitefox.adminpanel.dto.*;
import com.example.whitefox.adminpanel.dto.AdminTruckStatsResponse;
import com.example.whitefox.adminpanel.service.AdminPanelService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.example.whitefox.adminpanel.dto.AdminOrderResponse;
import com.example.whitefox.adminpanel.dto.AdminGarmentStatsResponse;
import java.util.List;
import com.example.whitefox.adminpanel.dto.AdminFinanceStatsResponse;
import com.example.whitefox.adminpanel.dto.AdminRiderStatsResponse;
import com.example.whitefox.adminpanel.dto.AdminBookingResponse;
import com.example.whitefox.adminpanel.dto.AdminCatalogStatsResponse;
@RestController
@RequestMapping("/api/admin-panel")
@RequiredArgsConstructor
public class AdminPanelController {

    private final AdminPanelService adminPanelService;

    @GetMapping("/dashboard")
    public AdminDashboardResponse getDashboard() {
        return adminPanelService.getDashboard();
    }
    @GetMapping("/stores/stats")
    public List<AdminStoreStatsResponse> getStoreStats() {
        return adminPanelService.getStoreStats();
    }
    @GetMapping("/riders/stats")
    public List<AdminRiderStatsResponse> getRiderStats() {
        return adminPanelService.getRiderStats();
    }
    @GetMapping("/garments/stats")
    public AdminGarmentStatsResponse getGarmentStats() {
        return adminPanelService.getGarmentStats();
    }
    @GetMapping("/customers/stats")
    public List<AdminCustomerStatsResponse> getCustomerStats() {
        return adminPanelService.getCustomerStats();
    }
    @GetMapping("/orders")
    public List<AdminOrderResponse> getAllOrders() {
        return adminPanelService.getAllOrdersForAdmin();
    }
    @GetMapping("/pickup-bills")
    public List<AdminPickupBillResponse> getPickupBills() {
        return adminPanelService.getAllPickupBillsForAdmin();
    }

    @GetMapping("/finance/stats")
    public AdminFinanceStatsResponse getFinanceStats() {
        return adminPanelService.getFinanceStats();
    }
    @GetMapping("/bookings")
    public List<AdminBookingResponse> getAllBookings() {
        return adminPanelService.getAllBookingsForAdmin();
    }
    @GetMapping("/reviews")
    public List<AdminReviewResponse> getAllReviews() {
        return adminPanelService.getAllReviewsForAdmin();
    }
    @GetMapping("/catalog/stats")
    public AdminCatalogStatsResponse getCatalogStats() {
        return adminPanelService.getCatalogStats();
    }
    @GetMapping("/search")
    public List<AdminSearchResponse> search(
            @RequestParam String keyword
    ) {
        return adminPanelService.search(keyword);
    }
    @GetMapping("/hq/operations")
    public AdminHqOperationsResponse getHqOperations() {
        return adminPanelService.getHqOperations();
    }
    @GetMapping("/trucks/stats")
    public AdminTruckStatsResponse getTruckStats() {
        return adminPanelService.getTruckStats();
    }
}
