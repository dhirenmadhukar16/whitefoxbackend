package com.example.whitefox.customerbooking.controller;



import com.example.whitefox.customerbooking.dto.*;
import com.example.whitefox.customerbooking.service.CustomerBookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/customer-bookings")
@RequiredArgsConstructor
public class CustomerBookingController {

    private final CustomerBookingService customerBookingService;

    @PostMapping
    public CustomerBookingResponse createBooking(
            @RequestBody CreateCustomerBookingRequest request
    ) {
        return customerBookingService.createBooking(request);
    }

    @GetMapping("/{bookingId}")
    public CustomerBookingResponse getBooking(
            @PathVariable UUID bookingId
    ) {
        return customerBookingService.getBooking(bookingId);
    }

    @GetMapping("/unassigned")
    public List<CustomerBookingResponse> getUnassignedBookings() {
        return customerBookingService.getUnassignedBookings();
    }

    @GetMapping("/customers/{customerId}")
    public List<CustomerBookingResponse> getBookingsByCustomer(
            @PathVariable UUID customerId
    ) {
        return customerBookingService.getBookingsByCustomer(customerId);
    }

    @GetMapping("/stores/{storeId}")
    public List<CustomerBookingResponse> getBookingsByStore(
            @PathVariable UUID storeId
    ) {
        return customerBookingService.getBookingsByStore(storeId);
    }

    @PatchMapping("/{bookingId}/assign-store")
    public CustomerBookingResponse assignStore(
            @PathVariable UUID bookingId,
            @RequestBody AssignStoreRequest request
    ) {
        return customerBookingService.assignStore(bookingId, request);
    }

    @PatchMapping("/{bookingId}/assign-rider")
    public CustomerBookingResponse assignRider(
            @PathVariable UUID bookingId,
            @RequestBody AssignRiderRequest request
    ) {
        return customerBookingService.assignRider(bookingId, request);
    }

    @PatchMapping("/{bookingId}/rider-on-the-way")
    public CustomerBookingResponse riderOnTheWay(
            @PathVariable UUID bookingId
    ) {
        return customerBookingService.markRiderOnTheWay(bookingId);
    }

    @PatchMapping("/{bookingId}/rider-reached")
    public CustomerBookingResponse riderReached(
            @PathVariable UUID bookingId
    ) {
        return customerBookingService.markRiderReached(bookingId);
    }

    @PatchMapping("/{bookingId}/pickup-bill-created")
    public CustomerBookingResponse pickupBillCreated(
            @PathVariable UUID bookingId
    ) {
        return customerBookingService.markPickupBillCreated(bookingId);
    }

    @PatchMapping("/{bookingId}/cancel")
    public CustomerBookingResponse cancelBooking(
            @PathVariable UUID bookingId
    ) {
        return customerBookingService.cancelBooking(bookingId);
    }
    
    @PatchMapping("/{bookingId}/reject-by-store")
    public CustomerBookingResponse rejectStoreAssignment(
            @PathVariable UUID bookingId,
            @RequestBody RejectBookingRequest request
    ) {
        return customerBookingService.rejectStoreAssignment(
                bookingId,
                request
        );
    }
    @PatchMapping("/{bookingId}/reschedule")
    public CustomerBookingResponse rescheduleBooking(
            @PathVariable UUID bookingId,
            @RequestBody RescheduleBookingRequest request
    ) {
        return customerBookingService.rescheduleBooking(
                bookingId,
                request
        );
    }
    @GetMapping("/riders/{riderId}")
    public List<CustomerBookingResponse> getBookingsByRider(
            @PathVariable UUID riderId
    ) {
        return customerBookingService.getBookingsByRider(riderId);
    }
}