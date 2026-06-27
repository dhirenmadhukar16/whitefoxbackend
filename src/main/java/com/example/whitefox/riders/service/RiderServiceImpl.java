package com.example.whitefox.riders.service;



import com.example.whitefox.riders.dto.*;
import com.example.whitefox.riders.entity.Rider;
import com.example.whitefox.riders.repository.RiderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RiderServiceImpl implements RiderService {

    private final RiderRepository riderRepository;

    @Override
    public RiderResponse createRider(CreateRiderRequest request) {

        Rider rider = Rider.builder()
                .name(request.getName())
                .phone(request.getPhone())
                .email(request.getEmail())
                .vehicleNumber(request.getVehicleNumber())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .build();

        Rider saved = riderRepository.save(rider);

        return map(saved);
    }

    @Override
    public List<RiderResponse> getAllRiders() {
        return riderRepository.findAll()
                .stream()
                .map(this::map)
                .toList();
    }

    @Override
    public RiderResponse getRider(UUID id) {

        Rider rider = riderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rider not found"));

        return map(rider);
    }

    @Override
    public RiderResponse updateRider(UUID id, CreateRiderRequest request) {

        Rider rider = riderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rider not found"));

        rider.setName(request.getName());
        rider.setPhone(request.getPhone());
        rider.setEmail(request.getEmail());
        rider.setVehicleNumber(request.getVehicleNumber());
        rider.setLatitude(request.getLatitude());
        rider.setLongitude(request.getLongitude());

        return map(riderRepository.save(rider));
    }

    @Override
    public RiderResponse updateStatus(UUID id, UpdateRiderStatusRequest request) {

        Rider rider = riderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rider not found"));

        rider.setStatus(request.getStatus());

        return map(riderRepository.save(rider));
    }

    @Override
    public void deactivateRider(UUID id) {

        Rider rider = riderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rider not found"));

        rider.setActive(false);

        riderRepository.save(rider);
    }

    private RiderResponse map(Rider rider) {
        return RiderResponse.builder()
                .id(rider.getId())
                .riderCode(rider.getRiderCode())
                .name(rider.getName())
                .phone(rider.getPhone())
                .email(rider.getEmail())
                .vehicleNumber(rider.getVehicleNumber())
                .latitude(rider.getLatitude())
                .longitude(rider.getLongitude())
                .status(rider.getStatus())
                .active(rider.getActive())
                .build();
    }
    @Override
    public RiderResponse updateLocation(UUID riderId, UpdateRiderLocationRequest request) {
        Rider rider = riderRepository.findById(riderId)
                .orElseThrow(() -> new RuntimeException("Rider not found"));

        rider.setLatitude(request.getLatitude());
        rider.setLongitude(request.getLongitude());
        rider.setSpeed(request.getSpeed());
        rider.setHeading(request.getHeading());
        rider.setLastSeen(java.time.LocalDateTime.now());

        return map(riderRepository.save(rider));
    }
}
