package com.example.whitefox.report.service;



import com.example.whitefox.orders.entity.LaundryOrder;
import com.example.whitefox.orders.enums.OrderStatus;
import com.example.whitefox.orders.repository.LaundryOrderRepository;
import com.example.whitefox.payment.enums.PaymentTransactionStatus;
import com.example.whitefox.payment.repository.PaymentRepository;
import com.example.whitefox.report.dto.ReportsResponse;
import com.example.whitefox.report.dto.StoreReportResponse;
import com.example.whitefox.store.entity.Store;
import com.example.whitefox.store.repository.StoreRepository;
import com.example.whitefox.tracking.entity.Garment;
import com.example.whitefox.tracking.enums.GarmentStatus;
import com.example.whitefox.tracking.repository.GarmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReportsServiceImpl implements ReportsService {

    private final LaundryOrderRepository orderRepository;
    private final GarmentRepository garmentRepository;
    private final PaymentRepository paymentRepository;
    private final StoreRepository storeRepository;

    @Override
    public ReportsResponse getOverallReport() {

        List<LaundryOrder> orders = orderRepository.findAll();
        List<Garment> garments = garmentRepository.findAll();

        double revenue = orders.stream()
                .filter(o -> o.getTotalAmount() != null)
                .mapToDouble(LaundryOrder::getTotalAmount)
                .sum();

        double collected = paymentRepository.findAll()
                .stream()
                .filter(p -> p.getStatus() == PaymentTransactionStatus.SUCCESS)
                .mapToDouble(p -> p.getAmount() == null ? 0.0 : p.getAmount())
                .sum();

        return ReportsResponse.builder()
                .totalOrders((long) orders.size())
                .deliveredOrders(orders.stream()
                        .filter(o -> o.getStatus() == OrderStatus.DELIVERED)
                        .count())
                .pendingOrders(orders.stream()
                        .filter(o -> o.getStatus() != OrderStatus.DELIVERED)
                        .count())
                .totalGarments((long) garments.size())
                .deliveredGarments(garments.stream()
                        .filter(g -> g.getStatus() == GarmentStatus.DELIVERED)
                        .count())
                .pendingGarments(garments.stream()
                        .filter(g -> g.getStatus() != GarmentStatus.DELIVERED)
                        .count())
                .totalRevenue(revenue)
                .totalPaymentsCollected(collected)
                .build();
    }

    @Override
    public StoreReportResponse getStoreReport(UUID storeId) {

        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new RuntimeException("Store not found"));

        List<LaundryOrder> orders = orderRepository.findAll()
                .stream()
                .filter(o -> o.getStore().getId().equals(storeId))
                .toList();

        List<Garment> garments = garmentRepository.findAll()
                .stream()
                .filter(g -> g.getOrder().getStore().getId().equals(storeId))
                .toList();

        double revenue = orders.stream()
                .filter(o -> o.getTotalAmount() != null)
                .mapToDouble(LaundryOrder::getTotalAmount)
                .sum();

        return StoreReportResponse.builder()
                .storeId(store.getId())
                .storeName(store.getName())
                .totalOrders((long) orders.size())
                .deliveredOrders(orders.stream()
                        .filter(o -> o.getStatus() == OrderStatus.DELIVERED)
                        .count())
                .pendingOrders(orders.stream()
                        .filter(o -> o.getStatus() != OrderStatus.DELIVERED)
                        .count())
                .totalGarments((long) garments.size())
                .revenue(revenue)
                .build();
    }
}
