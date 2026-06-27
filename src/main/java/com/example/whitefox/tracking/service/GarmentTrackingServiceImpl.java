package com.example.whitefox.tracking.service;

import com.example.whitefox.orders.entity.LaundryOrder;
import com.example.whitefox.orders.entity.OrderItem;
import com.example.whitefox.orders.repository.LaundryOrderRepository;
import com.example.whitefox.orders.repository.OrderItemRepository;
import com.example.whitefox.tracking.dto.*;
import com.example.whitefox.tracking.entity.Garment;
import com.example.whitefox.tracking.entity.GarmentTrackingHistory;
import com.example.whitefox.tracking.enums.GarmentStatus;
import com.example.whitefox.tracking.repository.GarmentRepository;
import com.example.whitefox.tracking.repository.GarmentTrackingHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GarmentTrackingServiceImpl implements GarmentTrackingService {

    private final GarmentRepository garmentRepository;
    private final GarmentTrackingHistoryRepository historyRepository;
    private final LaundryOrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

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
                .conditionNote(request.getConditionNote())
                .photoUrl(request.getPhotoUrl())
                .status(GarmentStatus.TAGGED_AT_STORE)
                .build();

        Garment saved = garmentRepository.save(garment);

        saveHistory(saved, GarmentStatus.TAGGED_AT_STORE, "Garment tagged at store");

        return map(saved);
    }

    @Override
    public List<GarmentResponse> generateGarmentsFromOrder(UUID orderId) {

        LaundryOrder order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        List<OrderItem> orderItems = orderItemRepository.findByOrderId(orderId);

        List<GarmentResponse> responses = new ArrayList<>();

        for (OrderItem item : orderItems) {

            for (int i = 1; i <= item.getQuantity(); i++) {

                Garment garment = Garment.builder()
                        .order(order)
                        .storeQrCode(generateStoreQrCode())
                        .itemName(item.getItemName())
                        .serviceType(item.getServiceType())
                        .status(GarmentStatus.TAGGED_AT_STORE)
                        .build();

                Garment saved = garmentRepository.save(garment);

                saveHistory(
                        saved,
                        GarmentStatus.TAGGED_AT_STORE,
                        "Garment auto-generated from order item"
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
                .storeQrCode(garment.getStoreQrCode())
                .outingQrCode(garment.getOutingQrCode())
                .itemName(garment.getItemName())
                .serviceType(garment.getServiceType())
                .color(garment.getColor())
                .conditionNote(garment.getConditionNote())
                .photoUrl(garment.getPhotoUrl())
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

        List<GarmentResponse> responses = new ArrayList<>();

        for (Garment garment : garments) {

            garment.setStatus(GarmentStatus.SENT_TO_HQ);

            Garment saved = garmentRepository.save(garment);

            saveHistory(
                    saved,
                    GarmentStatus.SENT_TO_HQ,
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
            garment.setStatus(GarmentStatus.SENT_TO_STORE);

            Garment saved = garmentRepository.save(garment);

            saveHistory(
                    saved,
                    GarmentStatus.SENT_TO_STORE,
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

        List<GarmentResponse> responses = new ArrayList<>();

        for (Garment garment : garments) {
            garment.setStatus(GarmentStatus.RECEIVED_AT_STORE_AFTER_PROCESSING);

            Garment saved = garmentRepository.save(garment);

            saveHistory(
                    saved,
                    GarmentStatus.RECEIVED_AT_STORE_AFTER_PROCESSING,
                    "Garment received back at store after HQ processing"
            );

            responses.add(map(saved));
        }

        return responses;
    }
}