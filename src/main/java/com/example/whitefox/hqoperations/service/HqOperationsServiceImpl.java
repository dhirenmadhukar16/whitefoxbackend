package com.example.whitefox.hqoperations.service;

import com.example.whitefox.hqoperations.dto.AttachHqOutingQrRequest;
import com.example.whitefox.hqoperations.dto.HqGarmentResponse;
import com.example.whitefox.hqoperations.dto.HqStoreDashboardResponse;
import com.example.whitefox.hqoperations.dto.HqCustomerDashboardResponse;
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
import com.example.whitefox.hqoperations.dto.HqReportResponse;
import java.util.stream.Collectors;
import java.util.Map;
import com.example.whitefox.realtime.service.RealtimeEventService;
import com.example.whitefox.realtime.dto.RealtimeEvent;

@Service
@RequiredArgsConstructor
public class HqOperationsServiceImpl implements HqOperationsService {

    private final GarmentRepository garmentRepository;
    private final LaundryOrderRepository orderRepository;
    private final RealtimeEventService realtimeEventService;

    @Override
    public HqGarmentResponse removeOldQr(UUID garmentId) {
        Garment garment = getGarment(garmentId);
        garment.setStoreQrCode(null);
        garment.setStatus(GarmentStatus.PROCESSING);
        Garment saved = garmentRepository.save(garment);
        sendRealtimeUpdate(saved);
        return map(saved);
    }

    @Override
    public HqGarmentResponse startCleaning(UUID garmentId) {
        Garment garment = getGarment(garmentId);
        garment.setStatus(GarmentStatus.PROCESSING);
        Garment saved = garmentRepository.save(garment);
        sendRealtimeUpdate(saved);
        return map(saved);
    }

    @Override
    public HqGarmentResponse markDelayed(UUID garmentId) {
        Garment garment = getGarment(garmentId);
        garment.setStatus(GarmentStatus.PROCESSING);
        Garment saved = garmentRepository.save(garment);
        sendRealtimeUpdate(saved);
        return map(saved);
    }

    @Override
    public HqGarmentResponse completeCleaning(UUID garmentId) {
        Garment garment = getGarment(garmentId);
        garment.setStatus(GarmentStatus.PROCESSING);
        Garment saved = garmentRepository.save(garment);
        sendRealtimeUpdate(saved);
        return map(saved);
    }

    @Override
    public HqGarmentResponse attachOutingQr(
            UUID garmentId,
            AttachHqOutingQrRequest request
    ) {
        Garment garment = getGarment(garmentId);
        garment.setOutingQrCode(request.getOutingQrCode());
        garment.setStatus(GarmentStatus.PROCESSED_QR_REATTACHED);
        Garment saved = garmentRepository.save(garment);
        sendRealtimeUpdate(saved);
        return map(saved);
    }

    @Override
    public HqGarmentResponse readyForDispatch(UUID garmentId) {
        Garment garment = getGarment(garmentId);
        garment.setStatus(GarmentStatus.PROCESSED_QR_REATTACHED);
        Garment saved = garmentRepository.save(garment);
        sendRealtimeUpdate(saved);
        return map(saved);
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
    public List<HqGarmentResponse> getUpcomingGarments() {
        return garmentRepository.findAll()
                .stream()
                .filter(g -> g.getStatus() == GarmentStatus.TAGGED_AT_STORE 
                          || g.getStatus() == GarmentStatus.LOADED_FOR_HQ)
                .map(this::map)
                .toList();
    }

    @Override
    public List<HqGarmentResponse> getInHqGarments() {
        return garmentRepository.findAll()
                .stream()
                .filter(g -> g.getStatus() == GarmentStatus.RECEIVED_AT_HQ 
                          || g.getStatus() == GarmentStatus.PROCESSING
                          || g.getStatus() == GarmentStatus.PROCESSED_QR_REATTACHED)
                .map(this::map)
                .toList();
    }

    @Override
    public List<HqStoreDashboardResponse> getStoreWiseDashboard() {
        List<Garment> allGarments = garmentRepository.findAll();
        List<LaundryOrder> allOrders = orderRepository.findAll();

        return allOrders.stream()
                .filter(o -> o.getStore() != null)
                .collect(Collectors.groupingBy(o -> o.getStore()))
                .entrySet()
                .stream()
                .map(entry -> {
                    var store = entry.getKey();
                    var orders = entry.getValue();
                    var storeGarments = allGarments.stream()
                            .filter(g -> g.getOrder().getStore().getId().equals(store.getId()))
                            .toList();

                    return HqStoreDashboardResponse.builder()
                            .storeId(store.getId())
                            .storeName(store.getName())
                            .orderCount((long) orders.size())
                            .garmentCount((long) storeGarments.size())
                            .garments(storeGarments.stream().map(this::map).toList())
                            .build();
                })
                .toList();
    }

    @Override
    public List<HqCustomerDashboardResponse> getCustomerWiseDashboard() {
        List<Garment> allGarments = garmentRepository.findAll();
        List<LaundryOrder> allOrders = orderRepository.findAll();

        return allOrders.stream()
                .filter(o -> o.getCustomer() != null)
                .collect(Collectors.groupingBy(o -> o.getCustomer()))
                .entrySet()
                .stream()
                .map(entry -> {
                    var customer = entry.getKey();
                    var orders = entry.getValue();
                    var customerGarments = allGarments.stream()
                            .filter(g -> g.getOrder().getCustomer().getId().equals(customer.getId()))
                            .toList();

                    return HqCustomerDashboardResponse.builder()
                            .customerId(customer.getId())
                            .customerName(customer.getName())
                            .customerPhone(customer.getPhone())
                            .orderCount((long) orders.size())
                            .garmentCount((long) customerGarments.size())
                            .garments(customerGarments.stream().map(this::map).toList())
                            .build();
                })
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

    @Override
    public HqReportResponse getHqReport(LocalDate startDate, LocalDate endDate) {
        List<LaundryOrder> orders = orderRepository.findAll().stream()
                .filter(o -> {
                    LocalDate d = o.getCreatedAt().toLocalDate();
                    return !d.isBefore(startDate) && !d.isAfter(endDate);
                })
                .toList();

        long comingToday = garmentRepository.findAll().stream()
                .filter(g -> g.getStatus() == GarmentStatus.LOADED_FOR_HQ)
                .count();

        long pending = garmentRepository.findAll().stream()
                .filter(g -> g.getStatus() == GarmentStatus.RECEIVED_AT_HQ || g.getStatus() == GarmentStatus.PROCESSING)
                .count();

        long dispatched = garmentRepository.findAll().stream()
                .filter(g -> g.getStatus() == GarmentStatus.LOADED_FOR_STORE && g.getUpdatedAt() != null && g.getUpdatedAt().toLocalDate().equals(LocalDate.now()))
                .count();

        long total = garmentRepository.count();

        Map<String, Long> storeWise = orders.stream()
                .filter(o -> o.getStore() != null)
                .collect(Collectors.groupingBy(o -> o.getStore().getName(), Collectors.counting()));

        List<HqReportResponse.StoreWiseOrder> storeWiseOrders = storeWise.entrySet().stream()
                .map(e -> HqReportResponse.StoreWiseOrder.builder()
                        .storeName(e.getKey())
                        .orderCount(e.getValue())
                        .build())
                .toList();

        Map<String, Long> customerWise = orders.stream()
                .filter(o -> o.getCustomer() != null)
                .collect(Collectors.groupingBy(o -> o.getCustomer().getName() + "___" + o.getCustomer().getPhone(), Collectors.counting()));

        List<HqReportResponse.CustomerWiseOrder> customerWiseOrders = customerWise.entrySet().stream()
                .map(e -> {
                    String[] parts = e.getKey().split("___");
                    return HqReportResponse.CustomerWiseOrder.builder()
                            .customerName(parts[0])
                            .customerPhone(parts.length > 1 ? parts[1] : "")
                            .orderCount(e.getValue())
                            .build();
                })
                .toList();

        return HqReportResponse.builder()
                .comingToday(comingToday)
                .pendingGarments(pending)
                .dispatchedToday(dispatched)
                .totalRealTime(total)
                .storeWiseOrders(storeWiseOrders)
                .customerWiseOrders(customerWiseOrders)
                .build();
    }

    private void sendRealtimeUpdate(Garment garment) {
        LaundryOrder order = garment.getOrder();
        if (order == null) return;
        
        RealtimeEvent event = RealtimeEvent.builder()
                .type("GARMENT_UPDATE")
                .title("Garment Update")
                .message("Garment " + garment.getItemName() + " is now " + garment.getStatus())
                .referenceId(order.getId())
                .status(garment.getStatus().name())
                .build();
                
        if (order.getCustomer() != null) {
            realtimeEventService.sendToCustomer(order.getCustomer().getId(), event);
        }
        if (order.getStore() != null) {
            realtimeEventService.sendToStore(order.getStore().getId(), event);
        }
    }
}