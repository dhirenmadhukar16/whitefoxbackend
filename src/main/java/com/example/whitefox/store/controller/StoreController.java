package com.example.whitefox.store.controller;
import org.springframework.http.ResponseEntity;
//package com.example.whitefox.controller;

//import com.example.whitefox.dto.store.CreateStoreRequest;
//import com.example.whitefox.dto.store.StoreResponse;
import com.example.whitefox.store.dto.CreateStoreRequest;
import com.example.whitefox.store.dto.StoreResponse;
import com.example.whitefox.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/stores")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    @PostMapping
    public StoreResponse createStore(
            @RequestBody CreateStoreRequest request
    ) {
        return storeService.createStore(request);
    }

    @GetMapping
    public List<StoreResponse> getAllStores() {
        return storeService.getAllStores();
    }

    @GetMapping("/{id}")
    public StoreResponse getStore(
            @PathVariable UUID id
    ) {
        return storeService.getStore(id);
    }
    @PutMapping("/{id}")
    public ResponseEntity<StoreResponse> updateStore(
            @PathVariable UUID id,
            @RequestBody CreateStoreRequest request) {

        return ResponseEntity.ok(
                storeService.updateStore(id, request)
        );
    }
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<String> deactivateStore(
            @PathVariable UUID id) {

        storeService.deactivateStore(id);

        return ResponseEntity.ok("Store deactivated successfully");
    }
}