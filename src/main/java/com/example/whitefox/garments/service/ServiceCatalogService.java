package com.example.whitefox.garments.service;



import com.example.whitefox.garments.dto.CreateServiceCatalogRequest;
import com.example.whitefox.garments.dto.ServiceCatalogResponse;

import com.example.whitefox.garments.dto.CreateServiceCategoryRequest;
import com.example.whitefox.garments.dto.ServiceCategoryResponse;

import java.util.List;
import java.util.UUID;

public interface ServiceCatalogService {

    ServiceCategoryResponse createCategory(CreateServiceCategoryRequest request);
    List<ServiceCategoryResponse> getAllCategories();

    ServiceCatalogResponse create(CreateServiceCatalogRequest request);

    List<ServiceCatalogResponse> getAll();

    ServiceCatalogResponse getById(UUID id);

    ServiceCatalogResponse update(
            UUID id,
            CreateServiceCatalogRequest request);

    void deactivate(UUID id);

    void delete(UUID id);
}