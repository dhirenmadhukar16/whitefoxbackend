package com.example.whitefox.storehistory.controller;

import com.example.whitefox.storehistory.dto.StoreOrderHistoryResponse;
import com.example.whitefox.storehistory.service.StoreHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/store/history")
@RequiredArgsConstructor
public class StoreHistoryController {

    private final StoreHistoryService service;

    @GetMapping("/{storeId}/today")
    public List<StoreOrderHistoryResponse> today(
            @PathVariable UUID storeId){

        return service.getTodayOrders(storeId);
    }

    @GetMapping("/{storeId}/yesterday")
    public List<StoreOrderHistoryResponse> yesterday(
            @PathVariable UUID storeId){

        return service.getYesterdayOrders(storeId);
    }

    @GetMapping("/{storeId}/last7days")
    public List<StoreOrderHistoryResponse> last7Days(
            @PathVariable UUID storeId){

        return service.getLast7Days(storeId);
    }

    @GetMapping("/{storeId}/last30days")
    public List<StoreOrderHistoryResponse> last30Days(
            @PathVariable UUID storeId){

        return service.getLast30Days(storeId);
    }

    @GetMapping("/{storeId}")
    public List<StoreOrderHistoryResponse> all(
            @PathVariable UUID storeId){

        return service.getAllHistory(storeId);
    }

}