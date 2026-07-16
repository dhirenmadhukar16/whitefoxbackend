package com.example.whitefox.riders.service;



import com.example.whitefox.riders.dto.*;
import com.example.whitefox.riders.dto.CreateRiderRequest;
import com.example.whitefox.riders.dto.RiderResponse;
import com.example.whitefox.riders.dto.UpdateRiderStatusRequest;

import java.util.List;
import java.util.UUID;

public interface RiderService {

    // Rider creation and modification removed from HQ/Admin
    RiderResponse getMe(String email);
    RiderResponse updateMe(String email, CompleteRiderProfileRequest request);

    List<RiderResponse> getAllRiders();

    RiderResponse getRider(UUID id);

    RiderResponse updateStatus(UUID id, UpdateRiderStatusRequest request);

    void deactivateRider(UUID id);
    RiderResponse updateLocation(UUID riderId, UpdateRiderLocationRequest request);
}
