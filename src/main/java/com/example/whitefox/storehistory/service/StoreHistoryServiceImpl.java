package com.example.whitefox.storehistory.service;

import com.example.whitefox.orders.entity.LaundryOrder;
import com.example.whitefox.orders.repository.LaundryOrderRepository;
import com.example.whitefox.storehistory.dto.StoreOrderHistoryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StoreHistoryServiceImpl implements StoreHistoryService {

    private final LaundryOrderRepository orderRepository;

    @Override
    public List<StoreOrderHistoryResponse> getTodayOrders(UUID storeId) {
        LocalDate today = LocalDate.now();
        return getOrdersBetween(
                storeId,
                today.atStartOfDay(),
                today.plusDays(1).atStartOfDay()
        );
    }

    @Override
    public List<StoreOrderHistoryResponse> getYesterdayOrders(UUID storeId) {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        return getOrdersBetween(
                storeId,
                yesterday.atStartOfDay(),
                yesterday.plusDays(1).atStartOfDay()
        );
    }

    @Override
    public List<StoreOrderHistoryResponse> getLast7Days(UUID storeId) {
        return getOrdersBetween(
                storeId,
                LocalDate.now().minusDays(7).atStartOfDay(),
                LocalDate.now().plusDays(1).atStartOfDay()
        );
    }

    @Override
    public List<StoreOrderHistoryResponse> getLast30Days(UUID storeId) {
        return getOrdersBetween(
                storeId,
                LocalDate.now().minusDays(30).atStartOfDay(),
                LocalDate.now().plusDays(1).atStartOfDay()
        );
    }

    @Override
    public List<StoreOrderHistoryResponse> getAllHistory(UUID storeId) {
        return orderRepository.findByStoreId(storeId)
                .stream()
                .map(this::map)
                .toList();
    }

    private List<StoreOrderHistoryResponse> getOrdersBetween(
            UUID storeId,
            LocalDateTime start,
            LocalDateTime end
    ) {
        return orderRepository.findByStoreId(storeId)
                .stream()
                .filter(order -> order.getCreatedAt() != null)
                .filter(order ->
                        !order.getCreatedAt().isBefore(start)
                                && order.getCreatedAt().isBefore(end)
                )
                .map(this::map)
                .toList();
    }

    private StoreOrderHistoryResponse map(LaundryOrder order) {
        return StoreOrderHistoryResponse.builder()
                .orderId(order.getId())
                .orderNumber(order.getOrderNumber())
                .customerName(order.getCustomer() != null ? order.getCustomer().getName() : null)
                .customerPhone(order.getCustomer() != null ? order.getCustomer().getPhone() : null)
                .riderName(order.getDeliveryRider() != null ? order.getDeliveryRider().getName() : null)
                .amount(order.getTotalAmount())
                .status(order.getStatus())
                .bookingTime(order.getCreatedAt())
                .pickupTime(null)
                .deliveryTime(order.getUpdatedAt())
                .build();
    }
}