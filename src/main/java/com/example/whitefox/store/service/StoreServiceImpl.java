package com.example.whitefox.store.service;



import com.example.whitefox.store.dto.CreateStoreRequest;
import com.example.whitefox.store.dto.StoreResponse;
import com.example.whitefox.store.entity.Store;
import com.example.whitefox.store.repository.StoreRepository;
import com.example.whitefox.store.service.StoreService;
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
public class StoreServiceImpl implements StoreService {

    private final StoreRepository storeRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private String generateRandomPassword() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[6];
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes); // Creates an 8-char string
    }

    @Override
    public StoreResponse createStore(CreateStoreRequest request) {

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
                .lastName("Store")
                .email(request.getEmail())
                .phone(request.getPhone())
                .password(passwordEncoder.encode(plainPassword))
                .role("STORE")
                .active(true)
                .build();

        userRepository.save(user);

        Store store = Store.builder()
                .storeCode(request.getStoreCode())
                .name(request.getName())
                .phone(request.getPhone())
                .email(request.getEmail())
                .address(request.getAddress())
                .city(request.getCity())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .build();

        storeRepository.save(store);

        StoreResponse response = map(store);
        response.setGeneratedPassword(plainPassword);
        return response;
    }

    @Override
    public List<StoreResponse> getAllStores() {
        return storeRepository.findAll()
                .stream()
                .map(this::map)
                .toList();
    }

    @Override
    public StoreResponse getStore(UUID id) {

        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Store not found"));

        return map(store);
    }

    private StoreResponse map(Store store) {
        return StoreResponse.builder()
                .id(store.getId())
                .storeCode(store.getStoreCode())
                .name(store.getName())
                .phone(store.getPhone())
                .email(store.getEmail())
                .address(store.getAddress())
                .city(store.getCity())
                .active(store.getActive())
                .build();
    }
    @Override
    public StoreResponse updateStore(
            UUID id,
            CreateStoreRequest request) {

        Store store = storeRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Store not found"));

        store.setName(request.getName());
        store.setPhone(request.getPhone());
        store.setEmail(request.getEmail());
        store.setAddress(request.getAddress());
        store.setCity(request.getCity());
        store.setLatitude(request.getLatitude());
        store.setLongitude(request.getLongitude());

        Store saved = storeRepository.save(store);

        return map(saved);
    }
    @Override
    public void deactivateStore(UUID id) {

        Store store = storeRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Store not found"));

        store.setActive(false);

        storeRepository.save(store);
    }
}