package com.example.whitefox.riders.service;



import com.example.whitefox.riders.dto.*;
import com.example.whitefox.riders.dto.CreateRiderRequest;
import com.example.whitefox.riders.dto.RiderResponse;
import com.example.whitefox.riders.dto.UpdateRiderStatusRequest;

import java.util.List;
import java.util.UUID;

public interface RiderService {

    RiderResponse createRider(CreateRiderRequest request);

    List<RiderResponse> getAllRiders();


    RiderResponse getRider(UUID id);

    RiderResponse updateRider(UUID id, CreateRiderRequest request);

    RiderResponse updateStatus(UUID id, UpdateRiderStatusRequest request);

    void deactivateRider(UUID id);
    RiderResponse updateLocation(UUID riderId, UpdateRiderLocationRequest request);
}
