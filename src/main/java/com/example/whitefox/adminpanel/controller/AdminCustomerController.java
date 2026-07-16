package com.example.whitefox.adminpanel.controller;

import com.example.whitefox.adminpanel.dto.CustomerCrmDto;
import com.example.whitefox.adminpanel.service.AdminCustomerService;
import com.example.whitefox.customerbooking.entity.CustomerBooking;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/customers")
@RequiredArgsConstructor
public class AdminCustomerController {

    private final AdminCustomerService adminCustomerService;

    @GetMapping
    public ResponseEntity<List<CustomerCrmDto>> getAllCustomersCrmData() {
        return ResponseEntity.ok(adminCustomerService.getAllCustomersCrmData());
    }

    @GetMapping("/{id}/orders")
    public ResponseEntity<List<CustomerBooking>> getCustomerOrders(@PathVariable UUID id) {
        return ResponseEntity.ok(adminCustomerService.getCustomerOrders(id));
    }
}
