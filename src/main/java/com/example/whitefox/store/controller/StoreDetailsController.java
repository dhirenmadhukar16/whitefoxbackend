package com.example.whitefox.store.controller;

import com.example.whitefox.store.service.StoreDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/admin-panel/stores/{storeId}")
@RequiredArgsConstructor
public class StoreDetailsController {

    private final StoreDetailsService storeDetailsService;

    @GetMapping("/orders")
    public ResponseEntity<?> getStoreOrders(@PathVariable UUID storeId) {
        return ResponseEntity.ok(storeDetailsService.getStoreOrders(storeId));
    }

    @GetMapping("/employees")
    public ResponseEntity<?> getStoreEmployees(@PathVariable UUID storeId) {
        return ResponseEntity.ok(storeDetailsService.getStoreEmployees(storeId));
    }

    @GetMapping("/inventory")
    public ResponseEntity<?> getStoreInventory(@PathVariable UUID storeId) {
        return ResponseEntity.ok(storeDetailsService.getStoreInventory(storeId));
    }

    @GetMapping("/customers")
    public ResponseEntity<?> getStoreCustomers(@PathVariable UUID storeId) {
        return ResponseEntity.ok(storeDetailsService.getStoreCustomers(storeId));
    }

    @GetMapping("/finance")
    public ResponseEntity<?> getStoreFinance(@PathVariable UUID storeId) {
        return ResponseEntity.ok(storeDetailsService.getStoreFinanceForToday(storeId));
    }

    @GetMapping("/reports")
    public ResponseEntity<?> getStoreReports(@PathVariable UUID storeId) {
        return ResponseEntity.ok(storeDetailsService.getStoreReports(storeId));
    }
}
