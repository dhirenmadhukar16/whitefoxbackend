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
    private final com.example.whitefox.store.repository.StoreRepository storeRepository;
    private final PasswordEncoder passwordEncoder;

    private String generateRandomPassword() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[6];
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes); // Creates an 8-char string
    }

    // Rider creation and update is now via self-signup and store approval

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

    // updateRider removed

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

    @Override
    public RiderResponse getMe(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found for email: " + email));
        Rider rider = riderRepository.findByPhone(user.getPhone())
                .orElseThrow(() -> new RuntimeException("Rider not found for phone: " + user.getPhone()));
        return map(rider);
    }

    @Override
    public RiderResponse updateMe(String email, CompleteRiderProfileRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found for email: " + email));
        Rider rider = riderRepository.findByPhone(user.getPhone())
                .orElseThrow(() -> new RuntimeException("Rider not found for phone: " + user.getPhone()));

        rider.setName(request.getName());
        
        com.example.whitefox.store.entity.Store store = storeRepository.findById(request.getStoreId())
                .orElseThrow(() -> new RuntimeException("Store not found"));
        
        rider.setStore(store);
        user.setStoreId(store.getId());

        riderRepository.save(rider);
        userRepository.save(user);

        return map(rider);
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
                .vehicleNumber(rider.getVehicleNumber())
                .status(rider.getStatus())
                .active(rider.getActive())
                .storeId(rider.getStore() != null ? rider.getStore().getId() : null)
                .whatsappNumber(rider.getWhatsappNumber())
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
