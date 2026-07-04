package com.example.whitefox.riders.service;



import com.example.whitefox.riders.dto.*;
import com.example.whitefox.riders.entity.Rider;
import com.example.whitefox.riders.repository.RiderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.example.whitefox.auth.entity.User;
import com.example.whitefox.auth.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.security.SecureRandom;
import java.util.Base64;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RiderServiceImpl implements RiderService {

    private final RiderRepository riderRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private String generateRandomPassword() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[6];
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes); // Creates an 8-char string
    }

    @Override
    public RiderResponse createRider(CreateRiderRequest request) {

        String plainPassword = request.getPassword();
        if (plainPassword == null || plainPassword.trim().isEmpty()) {
            plainPassword = generateRandomPassword();
        }

        // Check if user already exists
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        User user = User.builder()
                .firstName(request.getName())
                .lastName("Rider")
                .email(request.getEmail())
                .phone(request.getPhone())
                .password(passwordEncoder.encode(plainPassword))
                .role("RIDER")
                .active(true)
                .build();

        userRepository.save(user);

        Rider rider = Rider.builder()
                .name(request.getName())
                .phone(request.getPhone())
                .email(request.getEmail())
                .vehicleNumber(request.getVehicleNumber())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .build();

        Rider saved = riderRepository.save(rider);

        RiderResponse response = map(saved);
        response.setGeneratedPassword(plainPassword);
        return response;
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
