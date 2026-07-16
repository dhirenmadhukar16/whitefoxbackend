package com.example.whitefox.garments.controller;



import com.example.whitefox.garments.dto.CreateServiceCatalogRequest;
import com.example.whitefox.garments.dto.ServiceCatalogResponse;
import com.example.whitefox.garments.dto.ServiceCategoryResponse;
import com.example.whitefox.garments.service.ServiceCatalogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/catalog")
@RequiredArgsConstructor
public class ServiceCatalogController {

    private final ServiceCatalogService service;

    @PostMapping("/categories")
    public ServiceCategoryResponse createCategory(
            @RequestBody com.example.whitefox.garments.dto.CreateServiceCategoryRequest request) {
        return service.createCategory(request);
    }

    @GetMapping("/categories")
    public List<ServiceCategoryResponse> getAllCategories() {
        return service.getAllCategories();
    }

    @PostMapping
    public ServiceCatalogResponse create(
            @RequestBody CreateServiceCatalogRequest request) {

        return service.create(request);
    }

    @GetMapping
    public List<ServiceCatalogResponse> getAll() {

        return service.getAll();
    }

    @GetMapping("/{id}")
    public ServiceCatalogResponse getById(
            @PathVariable UUID id) {

        return service.getById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServiceCatalogResponse> update(
            @PathVariable UUID id,
            @RequestBody CreateServiceCatalogRequest request) {

        return ResponseEntity.ok(
                service.update(id, request));
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivate(
            @PathVariable UUID id) {

        service.deactivate(id);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}