package com.example.whitefox.garments.service;



import com.example.whitefox.garments.dto.CreateServiceCatalogRequest;
import com.example.whitefox.garments.dto.ServiceCatalogResponse;
import com.example.whitefox.garments.entity.ServiceCatalog;
import com.example.whitefox.garments.repository.ServiceCatalogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ServiceCatalogServiceImpl
        implements ServiceCatalogService {

    private final ServiceCatalogRepository repository;

    @Override
    public ServiceCatalogResponse create(
            CreateServiceCatalogRequest request) {

        ServiceCatalog service =
                ServiceCatalog.builder()
                        .serviceType(request.getServiceType())
                        .itemName(request.getItemName())
                        .price(request.getPrice())
                        .build();

        repository.save(service);

        return map(service);
    }

    @Override
    public List<ServiceCatalogResponse> getAll() {

        return repository.findAll()
                .stream()
                .map(this::map)
                .toList();
    }

    @Override
    public ServiceCatalogResponse getById(UUID id) {

        ServiceCatalog service =
                repository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException("Service not found"));

        return map(service);
    }

    @Override
    public ServiceCatalogResponse update(
            UUID id,
            CreateServiceCatalogRequest request) {

        ServiceCatalog service =
                repository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException("Service not found"));

        service.setServiceType(request.getServiceType());
        service.setItemName(request.getItemName());
        service.setPrice(request.getPrice());

        ServiceCatalog saved =
                repository.save(service);

        return map(saved);
    }

    @Override
    public void deactivate(UUID id) {

        ServiceCatalog service =
                repository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException("Service not found"));

        service.setActive(false);

        repository.save(service);
    }

    private ServiceCatalogResponse map(
            ServiceCatalog service) {

        return ServiceCatalogResponse.builder()
                .id(service.getId())
                .serviceType(service.getServiceType())
                .itemName(service.getItemName())
                .price(service.getPrice())
                .active(service.getActive())
                .build();
    }
}