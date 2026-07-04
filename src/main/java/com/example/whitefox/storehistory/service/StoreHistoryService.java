package com.example.whitefox.storehistory.service;

import com.example.whitefox.storehistory.dto.StoreOrderHistoryResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
@Service
public interface StoreHistoryService {

    List<StoreOrderHistoryResponse> getTodayOrders(UUID storeId);

    List<StoreOrderHistoryResponse> getYesterdayOrders(UUID storeId);

    List<StoreOrderHistoryResponse> getLast7Days(UUID storeId);

    List<StoreOrderHistoryResponse> getLast30Days(UUID storeId);

    List<StoreOrderHistoryResponse> getAllHistory(UUID storeId);

}
