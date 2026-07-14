package com.example.whitefox.tracking.service;



import com.example.whitefox.tracking.dto.*;

import java.util.List;
import java.util.UUID;

public interface GarmentTrackingService {

    GarmentResponse createGarment(
            UUID orderId,
            CreateGarmentRequest request
    );

    List<GarmentResponse> getGarmentsByOrder(
            UUID orderId
    );
    List<GarmentResponse> generateGarmentsFromOrder(UUID orderId, com.example.whitefox.tracking.dto.GenerateGarmentsRequest request);

    GarmentResponse updateStatus(
            UUID garmentId,
            UpdateGarmentStatusRequest request
    );

    GarmentResponse attachOutingQr(
            UUID garmentId,
            AttachOutingQrRequest request
    );

    GarmentResponse scanQr(
            String qrCode
    );
    GarmentResponse scanAction(String qrCode, String role);
    List<GarmentResponse> getGarmentsByStatus(String status);
    List<GarmentResponse> getAllHqGarments();
    List<GarmentResponse> sendOrderGarmentsToHq(UUID orderId);
    List<GarmentResponse> receiveOrderGarmentsAtHq(UUID orderId);
    List<GarmentResponse> sendOrderGarmentsBackToStore(UUID orderId);
    List<GarmentResponse> receiveOrderGarmentsBackAtStore(
            UUID orderId
    );

    GarmentResponse reportGarmentMissing(
            UUID garmentId
    );
}
