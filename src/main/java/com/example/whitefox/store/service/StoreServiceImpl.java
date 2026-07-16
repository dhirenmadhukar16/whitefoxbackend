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

        Store store = Store.builder()
                .storeCode(request.getStoreCode())
                .name(request.getName())
                .storeAdminName(request.getStoreAdminName())
                .phone(request.getPhone())
                .email(request.getEmail())
                .address(request.getAddress())
                .city(request.getCity())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .build();

        storeRepository.save(store);

        User user = User.builder()
                .firstName(request.getStoreAdminName() != null ? request.getStoreAdminName() : request.getName())
                .lastName("Store")
                .email(request.getEmail())
                .phone(request.getPhone())
                .password(passwordEncoder.encode(plainPassword))
                .role("STORE")
                .storeId(store.getId())
                .active(true)
                .build();

        userRepository.save(user);

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
        String loginEmail = userRepository.findFirstByStoreId(store.getId())
                .map(User::getEmail)
                .orElse(null);

        return StoreResponse.builder()
                .id(store.getId())
                .storeCode(store.getStoreCode())
                .name(store.getName())
                .storeAdminName(store.getStoreAdminName())
                .phone(store.getPhone())
                .email(store.getEmail())
                .loginEmail(loginEmail)
                .address(store.getAddress())
                .city(store.getCity())
                .latitude(store.getLatitude())
                .longitude(store.getLongitude())
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
        store.setStoreAdminName(request.getStoreAdminName());
        store.setPhone(request.getPhone());
        store.setEmail(request.getEmail());
        store.setAddress(request.getAddress());
        store.setCity(request.getCity());
        store.setLatitude(request.getLatitude());
        store.setLongitude(request.getLongitude());

        Store saved = storeRepository.save(store);

        // Sync user details
        userRepository.findFirstByStoreId(saved.getId()).ifPresent(user -> {
            user.setFirstName(request.getStoreAdminName() != null ? request.getStoreAdminName() : request.getName());
            user.setPhone(request.getPhone());
            user.setEmail(request.getEmail());
            userRepository.save(user);
        });

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

    @Override
    public void resetStorePassword(UUID storeId, String newPassword) {
        User storeUser = userRepository.findFirstByStoreId(storeId)
                .orElseThrow(() -> new RuntimeException("Store user not found"));
        
        storeUser.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(storeUser);
    }
}