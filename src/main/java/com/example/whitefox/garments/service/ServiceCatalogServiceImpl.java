package com.example.whitefox.garments.service;



import com.example.whitefox.garments.dto.CreateServiceCatalogRequest;
import com.example.whitefox.garments.dto.ServiceCatalogResponse;
import com.example.whitefox.garments.entity.ServiceCatalog;
import com.example.whitefox.garments.repository.ServiceCatalogRepository;
import com.example.whitefox.garments.dto.CreateServiceCategoryRequest;
import com.example.whitefox.garments.dto.ServiceCategoryResponse;
import com.example.whitefox.garments.entity.ServiceCategory;
import com.example.whitefox.garments.repository.ServiceCategoryRepository;
import com.example.whitefox.notification.dto.CreateNotificationRequest;
import com.example.whitefox.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ServiceCatalogServiceImpl
        implements ServiceCatalogService {

    private final ServiceCatalogRepository repository;
    private final ServiceCategoryRepository categoryRepository;
    private final NotificationService notificationService;

    @Override
    public ServiceCategoryResponse createCategory(CreateServiceCategoryRequest request) {
        ServiceCategory category = ServiceCategory.builder()
                .name(request.getName())
                .build();
        categoryRepository.save(category);
        return ServiceCategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .active(category.getActive())
                .build();
    }

    @Override
    public List<ServiceCategoryResponse> getAllCategories() {
        return categoryRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(c -> ServiceCategoryResponse.builder()
                        .id(c.getId())
                        .name(c.getName())
                        .active(c.getActive())
                        .build())
                .toList();
    }

    @Override
    public ServiceCatalogResponse create(
            CreateServiceCatalogRequest request) {

        ServiceCategory category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        ServiceCatalog service =
                ServiceCatalog.builder()
                        .category(category)
                        .itemName(request.getItemName())
                        .price(request.getPrice())
                        .thumbnailUrl(request.getThumbnailUrl())
                        .build();

        repository.save(service);

        sendPriceUpdateNotification("New Service Added", "A new service '" + service.getItemName() + "' has been added to the catalog.");

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

        ServiceCategory category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        service.setCategory(category);
        service.setItemName(request.getItemName());
        service.setPrice(request.getPrice());
        service.setThumbnailUrl(request.getThumbnailUrl());

        ServiceCatalog saved =
                repository.save(service);

        sendPriceUpdateNotification("Service Updated", "The service '" + saved.getItemName() + "' has been updated in the catalog.");

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

    @Override
    public void delete(UUID id) {
        repository.deleteById(id);
    }

    private void sendPriceUpdateNotification(String title, String message) {
        CreateNotificationRequest request = new CreateNotificationRequest();
        request.setTitle(title);
        request.setMessage(message);
        request.setSenderType("ADMIN");
        request.setReceiverType("STORE");
        request.setNotificationType("PRICE_UPDATE");
        notificationService.createNotification(request);
    }

    private ServiceCatalogResponse map(
            ServiceCatalog service) {

        return ServiceCatalogResponse.builder()
                .id(service.getId())
                .categoryId(service.getCategory().getId())
                .categoryName(service.getCategory().getName())
                .serviceType(service.getCategory().getName()) // For legacy
                .itemName(service.getItemName())
                .price(service.getPrice())
                .thumbnailUrl(service.getThumbnailUrl())
                .active(service.getActive())
                .build();
    }
}