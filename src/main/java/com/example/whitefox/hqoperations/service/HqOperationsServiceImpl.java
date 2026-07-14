package com.example.whitefox.hqoperations.service;

import com.example.whitefox.hqoperations.dto.AttachHqOutingQrRequest;
import com.example.whitefox.hqoperations.dto.HqGarmentResponse;
import com.example.whitefox.hqoperations.dto.HqStoreDispatchGroupResponse;
import com.example.whitefox.tracking.entity.Garment;
import com.example.whitefox.tracking.enums.GarmentStatus;
import com.example.whitefox.tracking.repository.GarmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.example.whitefox.hqoperations.dto.HqLiveOperationsResponse;
import com.example.whitefox.orders.entity.LaundryOrder;
import com.example.whitefox.orders.enums.OrderStatus;
import com.example.whitefox.orders.repository.LaundryOrderRepository;
import com.example.whitefox.storeops.dto.StoreOrderSummaryResponse;
import com.example.whitefox.hqoperations.dto.HqStoreDispatchGroupResponse;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class HqOperationsServiceImpl implements HqOperationsService {

    private final GarmentRepository garmentRepository;
    private final LaundryOrderRepository orderRepository;

    @Override
    public HqGarmentResponse removeOldQr(UUID garmentId) {
        Garment garment = getGarment(garmentId);
        garment.setStoreQrCode(null);
        garment.setStatus(GarmentStatus.PROCESSING);
        return map(garmentRepository.save(garment));
    }

    @Override
    public HqGarmentResponse startCleaning(UUID garmentId) {
        Garment garment = getGarment(garmentId);
        garment.setStatus(GarmentStatus.PROCESSING);
        return map(garmentRepository.save(garment));
    }

    @Override
    public HqGarmentResponse markDelayed(UUID garmentId) {
        Garment garment = getGarment(garmentId);
        garment.setStatus(GarmentStatus.PROCESSING);
        return map(garmentRepository.save(garment));
    }

    @Override
    public HqGarmentResponse completeCleaning(UUID garmentId) {
        Garment garment = getGarment(garmentId);
        garment.setStatus(GarmentStatus.PROCESSING);
        return map(garmentRepository.save(garment));
    }

    @Override
    public HqGarmentResponse attachOutingQr(
            UUID garmentId,
            AttachHqOutingQrRequest request
    ) {
        Garment garment = getGarment(garmentId);
        garment.setOutingQrCode(request.getOutingQrCode());
        garment.setStatus(GarmentStatus.PROCESSED_QR_REATTACHED);
        return map(garmentRepository.save(garment));
    }

    @Override
    public HqGarmentResponse readyForDispatch(UUID garmentId) {
        Garment garment = getGarment(garmentId);
        garment.setStatus(GarmentStatus.PROCESSED_QR_REATTACHED);
        return map(garmentRepository.save(garment));
    }

    @Override
    public List<HqGarmentResponse> getDelayedGarments() {
        return garmentRepository.findAll()
                .stream()
                .filter(g -> g.getStatus() == GarmentStatus.PROCESSING)
                .map(this::map)
                .toList();
    }

    @Override
    public List<HqGarmentResponse> getPendingDispatchByStore(UUID storeId) {
        return garmentRepository.findAll()
                .stream()
                .filter(g -> g.getOrder().getStore().getId().equals(storeId))
                .filter(g -> g.getStatus() == GarmentStatus.PROCESSING)
                .map(this::map)
                .toList();
    }

    @Override
    public List<HqGarmentResponse> getTodayDispatch() {
        return getDispatchByDate(LocalDate.now());
    }

    @Override
    public List<HqGarmentResponse> getYesterdayDispatch() {
        return getDispatchByDate(LocalDate.now().minusDays(1));
    }

    @Override
    public List<HqGarmentResponse> getTomorrowDispatch() {
        return getDispatchByDate(LocalDate.now().plusDays(1));
    }

    private List<HqGarmentResponse> getDispatchByDate(LocalDate date) {
        return garmentRepository.findAll()
                .stream()
                .filter(g -> g.getOrder().getDeliveryDate() != null)
                .filter(g -> g.getOrder().getDeliveryDate().equals(date))
                .map(this::map)
                .toList();
    }

    private Garment getGarment(UUID garmentId) {
        return garmentRepository.findById(garmentId)
                .orElseThrow(() -> new RuntimeException("Garment not found"));
    }

    private HqGarmentResponse map(Garment garment) {
        return HqGarmentResponse.builder()
                .garmentId(garment.getId())
                .orderId(garment.getOrder().getId())
                .orderNumber(garment.getOrder().getOrderNumber())
                .storeId(garment.getOrder().getStore().getId())
                .storeName(garment.getOrder().getStore().getName())
                .itemName(garment.getItemName())
                .serviceType(garment.getServiceType())
                .storeQrCode(garment.getStoreQrCode())
                .outingQrCode(garment.getOutingQrCode())
                .status(garment.getStatus())
                .build();
    }
    @Override
    public HqLiveOperationsResponse getLiveOperations() {

        List<Garment> garments = garmentRepository.findAll();
        List<LaundryOrder> orders = orderRepository.findAll();

        List<HqGarmentResponse> delayed = garments.stream()
                .filter(g -> g.getStatus() == GarmentStatus.PROCESSING)
                .map(this::map)
                .toList();

        List<HqGarmentResponse> todayDispatch = getTodayDispatch();

        List<HqGarmentResponse> readyDispatch = garments.stream()
                .filter(g -> g.getStatus() == GarmentStatus.PROCESSED_QR_REATTACHED)
                .map(this::map)
                .toList();

        List<StoreOrderSummaryResponse> latestOrders = orders.stream()
                .sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()))
                .limit(10)
                .map(this::mapOrder)
                .toList();

        return HqLiveOperationsResponse.builder()
                .totalGarments((long) garments.size())
                .delayedGarments((long) delayed.size())
                .cleaningInProgress(
                        garments.stream()
                                .filter(g -> g.getStatus() == GarmentStatus.PROCESSING)
                                .count()
                )
                .readyForDispatch((long) readyDispatch.size())
                .totalOrders((long) orders.size())
                .activeOrders(
                        orders.stream()
                                .filter(o -> o.getStatus() != OrderStatus.DELIVERED)
                                .count()
                )
                .delayed(delayed)
                .todayDispatch(todayDispatch)
                .readyDispatch(readyDispatch)
                .latestOrders(latestOrders)
                .build();
    }

    private StoreOrderSummaryResponse mapOrder(LaundryOrder order) {
        return StoreOrderSummaryResponse.builder()
                .orderId(order.getId())
                .orderNumber(order.getOrderNumber())
                .customerId(order.getCustomer().getId())
                .customerName(order.getCustomer().getName())
                .customerPhone(order.getCustomer().getPhone())
                .status(order.getStatus())
                .paymentStatus(order.getPaymentStatus())
                .totalAmount(order.getTotalAmount())
                .build();
    }
    @Override
    public List<HqStoreDispatchGroupResponse> getReadyDispatchGroupedByStore() {
        return garmentRepository.findAll()
                .stream()
                .filter(g -> g.getStatus() == GarmentStatus.PROCESSED_QR_REATTACHED)
                .collect(Collectors.groupingBy(g -> g.getOrder().getStore()))
                .entrySet()
                .stream()
                .map(entry -> HqStoreDispatchGroupResponse.builder()
                        .storeId(entry.getKey().getId())
                        .storeName(entry.getKey().getName())
                        .garmentCount((long) entry.getValue().size())
                        .garments(entry.getValue().stream().map(this::map).toList())
                        .build())
                .toList();
    }
}