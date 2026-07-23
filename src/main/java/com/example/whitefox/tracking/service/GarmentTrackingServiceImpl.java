package com.example.whitefox.tracking.service;

import com.example.whitefox.orders.entity.LaundryOrder;
import com.example.whitefox.orders.entity.OrderItem;
import com.example.whitefox.orders.repository.LaundryOrderRepository;
import com.example.whitefox.orders.repository.OrderItemRepository;
import com.example.whitefox.orders.enums.OrderStatus;
import com.example.whitefox.tracking.dto.*;
import com.example.whitefox.tracking.entity.Garment;
import com.example.whitefox.tracking.entity.GarmentTrackingHistory;
import com.example.whitefox.tracking.enums.GarmentStatus;
import com.example.whitefox.tracking.repository.GarmentRepository;
import com.example.whitefox.tracking.repository.GarmentTrackingHistoryRepository;
import com.example.whitefox.realtime.service.RealtimeEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class GarmentTrackingServiceImpl implements GarmentTrackingService {

    private final GarmentRepository garmentRepository;
    private final GarmentTrackingHistoryRepository historyRepository;
    private final LaundryOrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final RealtimeEventService realtimeEventService;

    @Override
    public GarmentResponse createGarment(
            UUID orderId,
            CreateGarmentRequest request
    ) {
        LaundryOrder order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        Garment garment = Garment.builder()
                .order(order)
                .storeQrCode(generateStoreQrCode())
                .itemName(request.getItemName())
                .serviceType(request.getServiceType())
                .color(request.getColor())
                .stains(request.getConditionNote())
                .photoUrls(java.util.Collections.singletonList(request.getPhotoUrl()))
                .status(GarmentStatus.TAGGED_AT_STORE)
                .build();

        Garment saved = garmentRepository.save(garment);

        saveHistory(saved, GarmentStatus.TAGGED_AT_STORE, "Garment tagged at store");

        return map(saved);
    }

    @Override
    public List<GarmentResponse> generateGarmentsFromOrder(UUID orderId, com.example.whitefox.tracking.dto.GenerateGarmentsRequest request) {

        LaundryOrder order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        List<GarmentResponse> responses = new ArrayList<>();

        if (request != null && request.getGarments() != null) {
            for (com.example.whitefox.tracking.dto.GarmentDetailsDto dto : request.getGarments()) {
                Garment garment = Garment.builder()
                        .order(order)
                        .storeQrCode(generateStoreQrCode())
                        .itemName(dto.getItemName())
                        .serviceType(dto.getServiceType())
                        .color(dto.getColor())
                        .stains(dto.getStains())
                        .specialInstructions(dto.getSpecialInstructions())
                        .photoUrls(dto.getPhotoUrls())
                        .status(GarmentStatus.TAGGED_AT_STORE)
                        .build();

                Garment saved = garmentRepository.save(garment);

                saveHistory(
                        saved,
                        GarmentStatus.TAGGED_AT_STORE,
                        "Garment tagged at store with details"
                );

                responses.add(map(saved));
            }
        }

        return responses;
    }

    @Override
    public List<GarmentResponse> getGarmentsByOrder(UUID orderId) {

        return garmentRepository.findByOrderId(orderId)
                .stream()
                .map(this::map)
                .toList();
    }

    @Override
    public GarmentResponse updateStatus(
            UUID garmentId,
            UpdateGarmentStatusRequest request
    ) {
        Garment garment = garmentRepository.findById(garmentId)
                .orElseThrow(() -> new RuntimeException("Garment not found"));

        garment.setStatus(request.getStatus());

        Garment saved = garmentRepository.save(garment);

        saveHistory(saved, request.getStatus(), request.getRemarks());

        return map(saved);
    }

    @Override
    public GarmentResponse attachOutingQr(
            UUID garmentId,
            AttachOutingQrRequest request
    ) {
        Garment garment = garmentRepository.findById(garmentId)
                .orElseThrow(() -> new RuntimeException("Garment not found"));

        garment.setOutingQrCode(request.getOutingQrCode());

        Garment saved = garmentRepository.save(garment);

        saveHistory(
                saved,
                saved.getStatus(),
                "QR reattached during packing: " + request.getOutingQrCode()
        );

        return map(saved);
    }

    @Override
    public GarmentResponse scanQr(String qrCode) {

        Garment garment = garmentRepository.findByStoreQrCode(qrCode)
                .or(() -> garmentRepository.findByOutingQrCode(qrCode))
                .orElseThrow(() -> new RuntimeException("QR not found"));

        return map(garment);
    }

    @Override
    public GarmentResponse scanAction(String qrCode, String role) {
        Garment garment = garmentRepository.findByStoreQrCode(qrCode)
                .or(() -> garmentRepository.findByOutingQrCode(qrCode))
                .orElseThrow(() -> new RuntimeException("QR not found"));

        GarmentStatus currentStatus = garment.getStatus();
        GarmentStatus newStatus = currentStatus;

        if ("TRUCK_DRIVER".equals(role)) {
            if (currentStatus == GarmentStatus.TAGGED_AT_STORE) {
                newStatus = GarmentStatus.LOADED_FOR_HQ;
            } else if (currentStatus == GarmentStatus.PROCESSED_QR_REATTACHED) {
                newStatus = GarmentStatus.LOADED_FOR_STORE;
            } else if (currentStatus == GarmentStatus.LOADED_FOR_STORE) {
                newStatus = GarmentStatus.DROPPED_AT_STORE;
            }
        } else if ("HQ_ADMIN".equals(role) || "HQ".equals(role)) {
            if (currentStatus == GarmentStatus.LOADED_FOR_HQ || currentStatus == GarmentStatus.DROPPED_AT_HQ) {
                newStatus = GarmentStatus.RECEIVED_AT_HQ;
            } else if (currentStatus == GarmentStatus.RECEIVED_AT_HQ) {
                newStatus = GarmentStatus.PROCESSING;
            } else if (currentStatus == GarmentStatus.PROCESSING) {
                newStatus = GarmentStatus.PROCESSED_QR_REATTACHED;
            }
        } else if ("STORE_MANAGER".equals(role) || "STORE".equals(role)) {
            if (currentStatus == GarmentStatus.LOADED_FOR_STORE) {
                newStatus = GarmentStatus.DROPPED_AT_STORE;
            }
        }

        if (newStatus != currentStatus) {
            garment.setStatus(newStatus);
            garment = garmentRepository.save(garment);
            saveHistory(garment, newStatus, "Status updated via smart scan by " + role);

            checkAndUpdateOrderStatus(garment.getOrder(), newStatus);
        }

        return map(garment);
    }

    @Override
    public List<GarmentResponse> getGarmentsByStatus(String status) {
        try {
            GarmentStatus garmentStatus = GarmentStatus.valueOf(status.toUpperCase());
            return garmentRepository.findByStatus(garmentStatus)
                    .stream()
                    .map(this::map)
                    .toList();
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid status: " + status);
        }
    }

    @Override
    public List<GarmentResponse> getAllHqGarments() {
        // Return all garments that are currently at HQ (in any HQ processing stage)
        List<GarmentStatus> hqStatuses = List.of(
                GarmentStatus.RECEIVED_AT_HQ,
                GarmentStatus.PROCESSING,
                GarmentStatus.PROCESSED_QR_REATTACHED
        );
        return garmentRepository.findByStatusIn(hqStatuses)
                .stream()
                .map(this::map)
                .toList();
    }


    private void checkAndUpdateOrderStatus(LaundryOrder order, GarmentStatus newGarmentStatus) {

        List<Garment> garments = garmentRepository.findByOrderId(order.getId());
        
        long validGarmentCount = garments.stream()
                .filter(g -> g.getStatus() != GarmentStatus.REPORTED_MISSING)
                .count();

        long matchingGarmentCount = garments.stream()
                .filter(g -> g.getStatus() == newGarmentStatus)
                .count();

        boolean allMatch = (validGarmentCount > 0) && (matchingGarmentCount == validGarmentCount);

        if (allMatch) {
            if (newGarmentStatus == GarmentStatus.LOADED_FOR_HQ) {
                order.setStatus(OrderStatus.SENT_TO_HQ);
            } else if (newGarmentStatus == GarmentStatus.RECEIVED_AT_HQ) {
                order.setStatus(OrderStatus.RECEIVED_AT_HQ);
            } else if (newGarmentStatus == GarmentStatus.LOADED_FOR_STORE) {
                order.setStatus(OrderStatus.READY_FOR_DELIVERY); 
            } else if (newGarmentStatus == GarmentStatus.DROPPED_AT_STORE) {
                order.setStatus(OrderStatus.RECEIVED_AT_STORE_AFTER_PROCESSING);
            } else if (newGarmentStatus == GarmentStatus.READY_FOR_DELIVERY) {
                order.setStatus(OrderStatus.READY_FOR_DELIVERY);
            } else if (newGarmentStatus == GarmentStatus.READY_FOR_CUSTOMER_PICKUP) {
                order.setStatus(OrderStatus.READY_FOR_CUSTOMER_PICKUP);
            }
            orderRepository.save(order);
        }
    }

    private String generateStoreQrCode() {
        return "STORE-QR-" + System.currentTimeMillis() + "-" + UUID.randomUUID();
    }

    private void saveHistory(
            Garment garment,
            GarmentStatus status,
            String remarks
    ) {
        GarmentTrackingHistory history = GarmentTrackingHistory.builder()
                .garment(garment)
                .status(status)
                .remarks(remarks)
                .build();

        historyRepository.save(history);
    }

    private GarmentResponse map(Garment garment) {

        List<TrackingHistoryResponse> history =
                historyRepository.findByGarmentId(garment.getId())
                        .stream()
                        .map(h -> TrackingHistoryResponse.builder()
                                .status(h.getStatus())
                                .remarks(h.getRemarks())
                                .createdAt(h.getCreatedAt())
                                .build()
                        )
                        .toList();

        return GarmentResponse.builder()
                .id(garment.getId())
                .orderId(garment.getOrder().getId())
                .storeId(garment.getOrder().getStore() != null ? garment.getOrder().getStore().getId() : null)
                .storeName(garment.getOrder().getStore() != null ? garment.getOrder().getStore().getName() : null)
                .storeQrCode(garment.getStoreQrCode())
                .outingQrCode(garment.getOutingQrCode())
                .itemName(garment.getItemName())
                .serviceType(garment.getServiceType())
                .color(garment.getColor())
                .stains(garment.getStains())
                .specialInstructions(garment.getSpecialInstructions())
                .photoUrls(garment.getPhotoUrls())
                .customerName(garment.getOrder() != null && garment.getOrder().getCustomer() != null ? garment.getOrder().getCustomer().getName() : null)
                .customerPhone(garment.getOrder() != null && garment.getOrder().getCustomer() != null ? garment.getOrder().getCustomer().getPhone() : null)
                .customerId(garment.getOrder() != null && garment.getOrder().getCustomer() != null ? garment.getOrder().getCustomer().getId() : null)
                .status(garment.getStatus())
                .history(history)
                .build();
    }
    @Override
    public List<GarmentResponse> sendOrderGarmentsToHq(UUID orderId) {

        List<Garment> garments = garmentRepository.findByOrderId(orderId);

        if (garments.isEmpty()) {
            throw new RuntimeException("No garments found for this order");
        }

        LaundryOrder order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(OrderStatus.SENT_TO_HQ);
        orderRepository.save(order);

        List<GarmentResponse> responses = new ArrayList<>();

        for (Garment garment : garments) {

            garment.setStatus(GarmentStatus.LOADED_FOR_HQ);

            Garment saved = garmentRepository.save(garment);

            saveHistory(
                    saved,
                    GarmentStatus.LOADED_FOR_HQ,
                    "Garment sent from store to HQ"
            );

            responses.add(map(saved));
        }

        return responses;
    }
    @Override
    public List<GarmentResponse> receiveOrderGarmentsAtHq(UUID orderId) {

        List<Garment> garments = garmentRepository.findByOrderId(orderId);

        if (garments.isEmpty()) {
            throw new RuntimeException("No garments found for this order");
        }

        List<GarmentResponse> responses = new ArrayList<>();

        for (Garment garment : garments) {
            garment.setStatus(GarmentStatus.RECEIVED_AT_HQ);

            Garment saved = garmentRepository.save(garment);

            saveHistory(
                    saved,
                    GarmentStatus.RECEIVED_AT_HQ,
                    "Garment received at HQ"
            );

            responses.add(map(saved));
        }

        return responses;
    }
    @Override
    public List<GarmentResponse> sendOrderGarmentsBackToStore(UUID orderId) {

        List<Garment> garments = garmentRepository.findByOrderId(orderId);

        if (garments.isEmpty()) {
            throw new RuntimeException("No garments found for this order");
        }

        List<GarmentResponse> responses = new ArrayList<>();

        for (Garment garment : garments) {
            garment.setStatus(GarmentStatus.LOADED_FOR_STORE);

            Garment saved = garmentRepository.save(garment);

            saveHistory(
                    saved,
                    GarmentStatus.LOADED_FOR_STORE,
                    "Garment dispatched from HQ back to store"
            );

            responses.add(map(saved));
        }

        return responses;
    }
    @Override
    public List<GarmentResponse> receiveOrderGarmentsBackAtStore(UUID orderId) {

        List<Garment> garments = garmentRepository.findByOrderId(orderId);

        if (garments.isEmpty()) {
            throw new RuntimeException("No garments found for this order");
        }

        LaundryOrder order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(OrderStatus.READY_FOR_DELIVERY);
        orderRepository.save(order);

        List<GarmentResponse> responses = new ArrayList<>();

        for (Garment garment : garments) {
            garment.setStatus(GarmentStatus.DROPPED_AT_STORE);

            Garment saved = garmentRepository.save(garment);

            saveHistory(
                    saved,
                    GarmentStatus.DROPPED_AT_STORE,
                    "Garment received back at store after HQ processing"
            );

            responses.add(map(saved));
        }

        return responses;
    }

    @Override
    public GarmentResponse reportGarmentMissing(UUID garmentId) {
        Garment garment = garmentRepository.findById(garmentId)
                .orElseThrow(() -> new RuntimeException("Garment not found"));

        garment.setStatus(GarmentStatus.REPORTED_MISSING);
        Garment saved = garmentRepository.save(garment);

        saveHistory(
                saved,
                GarmentStatus.REPORTED_MISSING,
                "Garment reported missing"
        );

        return map(saved);
    }

    @Override
    public List<TruckRouteResponse> getTruckPickupRoutes() {
        List<Garment> fromStore = garmentRepository.findByStatus(GarmentStatus.TAGGED_AT_STORE);
        List<Garment> fromHq = garmentRepository.findByStatus(GarmentStatus.PROCESSED_QR_REATTACHED);
        
        List<TruckRouteResponse> storeRoutes = groupGarmentsByStore(fromStore, "PICKUP_FROM_STORE");
        List<TruckRouteResponse> hqRoutes = groupGarmentsByStore(fromHq, "PICKUP_FROM_HQ");
        
        List<TruckRouteResponse> allRoutes = new ArrayList<>();
        allRoutes.addAll(storeRoutes);
        allRoutes.addAll(hqRoutes);
        return allRoutes;
    }

    @Override
    public List<TruckRouteResponse> getTruckDropRoutes() {
        List<Garment> forStore = garmentRepository.findByStatus(GarmentStatus.LOADED_FOR_STORE);
        List<Garment> forHq = garmentRepository.findByStatus(GarmentStatus.LOADED_FOR_HQ);
        
        List<TruckRouteResponse> storeRoutes = groupGarmentsByStore(forStore, "DROP_AT_STORE");
        List<TruckRouteResponse> hqRoutes = groupGarmentsByStore(forHq, "DROP_AT_HQ");
        
        List<TruckRouteResponse> allRoutes = new ArrayList<>();
        allRoutes.addAll(storeRoutes);
        allRoutes.addAll(hqRoutes);
        return allRoutes;
    }

    @Override
    public List<TrackingHistoryResponse> getTruckHistory(String date, String role) {
        // Find history records performed by TRUCK_DRIVER on the specific date
        // Note: simplified to return all history for that role for now
        List<GarmentTrackingHistory> histories = historyRepository.findAll().stream()
                .filter(h -> h.getRemarks() != null && h.getRemarks().contains(role))
                .toList();

        return histories.stream()
                .map(h -> TrackingHistoryResponse.builder()
                        .status(h.getStatus())
                        .remarks(h.getRemarks())
                        .createdAt(h.getCreatedAt())
                        .itemName(h.getGarment().getItemName())
                        .storeQrCode(h.getGarment().getStoreQrCode())
                        .storeName(h.getGarment().getOrder().getStore().getName())
                        .build())
                .toList();
    }

    @Override
    public List<GarmentResponse> approveStoreDrops(UUID storeId) {
        List<Garment> dropped = garmentRepository.findByStatus(GarmentStatus.DROPPED_AT_STORE).stream()
                .filter(g -> g.getOrder().getStore().getId().equals(storeId))
                .toList();

        List<GarmentResponse> responses = new ArrayList<>();
        for (Garment garment : dropped) {
            String deliveryType = garment.getOrder().getDeliveryType() != null ? garment.getOrder().getDeliveryType().name() : "";
            GarmentStatus newStatus = GarmentStatus.READY_FOR_DELIVERY;
            if ("SELF_PICKUP".equalsIgnoreCase(deliveryType)) {
                newStatus = GarmentStatus.READY_FOR_CUSTOMER_PICKUP;
            }
            
            garment.setStatus(newStatus);
            Garment saved = garmentRepository.save(garment);
            saveHistory(saved, newStatus, "Store approved drop");
            responses.add(map(saved));
        }
        return responses;
    }

    private List<TruckRouteResponse> groupGarmentsByStore(List<Garment> garments, String routeType) {
        java.util.Map<com.example.whitefox.store.entity.Store, List<Garment>> byStore = new java.util.HashMap<>();
        for (Garment g : garments) {
            com.example.whitefox.store.entity.Store s = g.getOrder().getStore();
            byStore.computeIfAbsent(s, k -> new ArrayList<>()).add(g);
        }

        List<TruckRouteResponse> responses = new ArrayList<>();
        for (java.util.Map.Entry<com.example.whitefox.store.entity.Store, List<Garment>> entry : byStore.entrySet()) {
            responses.add(new TruckRouteResponse(
                    mapStore(entry.getKey()),
                    routeType,
                    entry.getValue().stream().map(this::map).toList()
            ));
        }
        return responses;
    }

    private com.example.whitefox.store.dto.StoreResponse mapStore(com.example.whitefox.store.entity.Store store) {
        if (store == null) return null;
        return com.example.whitefox.store.dto.StoreResponse.builder()
                .id(store.getId())
                .name(store.getName())
                .address(store.getAddress())
                // Assuming phone is available. Let's just map the basics.
                .build();
    }

    @Override
    public List<GarmentResponse> pickupStore(UUID storeId) {
        List<Garment> fromStore = garmentRepository.findByStatus(GarmentStatus.TAGGED_AT_STORE).stream()
                .filter(g -> g.getOrder().getStore().getId().equals(storeId))
                .toList();
        List<Garment> fromHq = garmentRepository.findByStatus(GarmentStatus.PROCESSED_QR_REATTACHED).stream()
                .filter(g -> g.getOrder().getStore().getId().equals(storeId))
                .toList();
                
        List<GarmentResponse> responses = new ArrayList<>();
        for (Garment g : fromStore) {
            g.setStatus(GarmentStatus.LOADED_FOR_HQ);
            Garment saved = garmentRepository.save(g);
            saveHistory(saved, GarmentStatus.LOADED_FOR_HQ, "Picked up from store by truck");
            responses.add(map(saved));
        }
        for (Garment g : fromHq) {
            g.setStatus(GarmentStatus.LOADED_FOR_STORE);
            Garment saved = garmentRepository.save(g);
            saveHistory(saved, GarmentStatus.LOADED_FOR_STORE, "Picked up from HQ by truck");
            responses.add(map(saved));
        }
        return responses;
    }

    @Override
    public List<GarmentResponse> dropHq() {
        List<Garment> loadedForHq = garmentRepository.findByStatus(GarmentStatus.LOADED_FOR_HQ);
        List<GarmentResponse> responses = new ArrayList<>();
        
        for (Garment g : loadedForHq) {
            g.setStatus(GarmentStatus.DROPPED_AT_HQ);
            Garment saved = garmentRepository.save(g);
            saveHistory(saved, GarmentStatus.DROPPED_AT_HQ, "Dropped at HQ by truck driver");
            responses.add(map(saved));
        }
        
        if (!responses.isEmpty()) {
            com.example.whitefox.realtime.dto.RealtimeEvent event = com.example.whitefox.realtime.dto.RealtimeEvent.builder()
                    .type("TRUCK_DROP_AT_HQ")
                    .title("New Garments Dropped at HQ")
                    .message(responses.size() + " garments were dropped at HQ. Please verify and approve.")
                    .build();
            realtimeEventService.sendToAdmin(event);
        }
        
        return responses;
    }

    @Override
    public List<GarmentResponse> approveDrop() {
        List<Garment> droppedAtHq = garmentRepository.findByStatus(GarmentStatus.DROPPED_AT_HQ);
        List<GarmentResponse> responses = new ArrayList<>();
        
        for (Garment g : droppedAtHq) {
            g.setStatus(GarmentStatus.RECEIVED_AT_HQ);
            Garment saved = garmentRepository.save(g);
            saveHistory(saved, GarmentStatus.RECEIVED_AT_HQ, "Garment received at HQ");
            checkAndUpdateOrderStatus(g.getOrder(), GarmentStatus.RECEIVED_AT_HQ);
            responses.add(map(saved));
        }
        return responses;
    }

    @Override
    public List<GarmentResponse> getDispatchedHistory(String dateStr) {
        List<GarmentStatus> statuses = List.of(
            GarmentStatus.LOADED_FOR_STORE,
            GarmentStatus.DROPPED_AT_STORE,
            GarmentStatus.READY_FOR_DELIVERY,
            GarmentStatus.OUT_FOR_DELIVERY,
            GarmentStatus.DELIVERED,
            GarmentStatus.READY_FOR_CUSTOMER_PICKUP
        );
        
        List<Garment> allDispatched = garmentRepository.findByStatusIn(statuses);
        
        if (dateStr == null || dateStr.isEmpty()) {
            dateStr = java.time.LocalDate.now().toString();
        }
        
        final String targetDate = dateStr;
        return allDispatched.stream()
                .filter(g -> g.getUpdatedAt() != null && g.getUpdatedAt().toLocalDate().toString().equals(targetDate))
                .map(this::map)
                .toList();
    }
}