package com.example.whitefox.store.service;





import com.example.whitefox.store.dto.CreateStoreRequest;
import com.example.whitefox.store.dto.StoreResponse;

import java.util.List;
import java.util.UUID;

public interface StoreService {

    StoreResponse createStore(CreateStoreRequest request);

    List<StoreResponse> getAllStores();

    StoreResponse getStore(UUID id);
    StoreResponse updateStore(UUID id, CreateStoreRequest request);
    void deactivateStore(UUID id);

    void resetStorePassword(UUID storeId, String newPassword);
}