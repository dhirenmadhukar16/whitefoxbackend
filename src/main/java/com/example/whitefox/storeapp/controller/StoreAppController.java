package com.example.whitefox.storeapp.controller;

import com.example.whitefox.customerbooking.dto.CustomerBookingResponse;
import com.example.whitefox.orders.dto.OrderResponse;
import com.example.whitefox.riders.dto.RiderResponse;
import com.example.whitefox.storeemployee.dto.CreateStoreEmployeeRequest;
import com.example.whitefox.storeemployee.dto.StoreEmployeeResponse;
import com.example.whitefox.storeemployee.dto.UpdateStoreEmployeeRequest;
import com.example.whitefox.storeapp.service.StoreAppService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/store-app")
@RequiredArgsConstructor
public class StoreAppController {

    private final StoreAppService storeAppService;

    @GetMapping("/stores/{storeId}/employees")
    public List<StoreEmployeeResponse> getEmployees(@PathVariable UUID storeId) {
        return storeAppService.getEmployees(storeId);
    }

    @PostMapping("/stores/{storeId}/employees")
    public StoreEmployeeResponse addEmployee(
            @PathVariable UUID storeId,
            @RequestBody CreateStoreEmployeeRequest request
    ) {
        return storeAppService.addEmployee(storeId, request);
    }

    @PutMapping("/employees/{employeeId}")
    public StoreEmployeeResponse updateEmployee(
            @PathVariable UUID employeeId,
            @RequestBody UpdateStoreEmployeeRequest request
    ) {
        return storeAppService.updateEmployee(employeeId, request);
    }

    @PatchMapping("/employees/{employeeId}/deactivate")
    public void deactivateEmployee(@PathVariable UUID employeeId) {
        storeAppService.deactivateEmployee(employeeId);
    }

    @GetMapping("/riders")
    public List<RiderResponse> getRiders() {
        return storeAppService.getRiders();
    }

    @PatchMapping("/bookings/{bookingId}/assign-rider/{riderId}")
    public CustomerBookingResponse assignRiderToBooking(
            @PathVariable UUID bookingId,
            @PathVariable UUID riderId
    ) {
        return storeAppService.assignRiderToBooking(bookingId, riderId);
    }

    @PatchMapping("/orders/{orderId}/assign-delivery-rider/{riderId}")
    public OrderResponse assignDeliveryRiderToOrder(
            @PathVariable UUID orderId,
            @PathVariable UUID riderId
    ) {
        return storeAppService.assignDeliveryRiderToOrder(orderId, riderId);
    }
    @GetMapping("/stores/{storeId}/live-riders")
    public List<RiderResponse> getLiveRiders(@PathVariable UUID storeId) {
        return storeAppService.getRiders();
    }
}