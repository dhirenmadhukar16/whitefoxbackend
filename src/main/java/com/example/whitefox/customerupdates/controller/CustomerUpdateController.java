package com.example.whitefox.customerupdates.controller;


import com.example.whitefox.customerupdates.dto.CreateCustomerUpdateRequest;
import com.example.whitefox.customerupdates.dto.CustomerUpdateResponse;
import com.example.whitefox.customerupdates.service.CustomerUpdateService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/customer-updates")
@RequiredArgsConstructor
public class CustomerUpdateController {

    private final CustomerUpdateService updateService;

    @PostMapping
    public CustomerUpdateResponse createUpdate(
            @RequestBody CreateCustomerUpdateRequest request
    ) {
        return updateService.createUpdate(request);
    }

    @GetMapping("/customers/{customerId}")
    public List<CustomerUpdateResponse> getCustomerUpdates(
            @PathVariable UUID customerId
    ) {
        return updateService.getUpdatesByCustomer(customerId);
    }

    @GetMapping("/orders/{orderId}/timeline")
    public List<CustomerUpdateResponse> getOrderTimeline(
            @PathVariable UUID orderId
    ) {
        return updateService.getTimelineByOrder(orderId);
    }

    @PatchMapping("/{updateId}/read")
    public CustomerUpdateResponse markAsRead(
            @PathVariable UUID updateId
    ) {
        return updateService.markAsRead(updateId);
    }
}