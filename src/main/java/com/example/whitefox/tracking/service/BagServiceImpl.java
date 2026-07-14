package com.example.whitefox.tracking.service;

import com.example.whitefox.store.entity.Store;
import com.example.whitefox.store.repository.StoreRepository;
import com.example.whitefox.tracking.dto.BagResponse;
import com.example.whitefox.tracking.dto.CreateBagRequest;
import com.example.whitefox.tracking.entity.Bag;
import com.example.whitefox.tracking.entity.Garment;
import com.example.whitefox.tracking.enums.GarmentStatus;
import com.example.whitefox.tracking.repository.BagRepository;
import com.example.whitefox.tracking.repository.GarmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BagServiceImpl implements BagService {

    private final BagRepository bagRepository;
    private final StoreRepository storeRepository;
    private final GarmentRepository garmentRepository;

    @Override
    public BagResponse createBag(CreateBagRequest request) {
        Store source = null;
        if (request.getSourceStoreId() != null) {
            source = storeRepository.findById(request.getSourceStoreId())
                    .orElseThrow(() -> new RuntimeException("Source store not found"));
        }

        Store dest = null;
        if (request.getDestinationStoreId() != null) {
            dest = storeRepository.findById(request.getDestinationStoreId())
                    .orElseThrow(() -> new RuntimeException("Destination store not found"));
        }

        Bag bag = Bag.builder()
                .qrCode(request.getQrCode())
                .sourceStore(source)
                .destinationStore(dest)
                .status("CREATED")
                .build();
        
        Bag savedBag = bagRepository.save(bag);

        if (request.getGarmentIds() != null && !request.getGarmentIds().isEmpty()) {
            List<Garment> garments = garmentRepository.findAllById(request.getGarmentIds());
            for (Garment g : garments) {
                g.setCurrentBag(savedBag);
                g.setStatus(GarmentStatus.LOADED_FOR_HQ);
            }
            garmentRepository.saveAll(garments);
            savedBag.setGarments(garments);
        }

        return mapBag(savedBag);
    }

    @Override
    public BagResponse getBag(UUID bagId) {
        Bag bag = bagRepository.findById(bagId).orElseThrow(() -> new RuntimeException("Bag not found"));
        return mapBag(bag);
    }

    @Override
    public BagResponse getBagByQrCode(String qrCode) {
        Bag bag = bagRepository.findByQrCode(qrCode).orElseThrow(() -> new RuntimeException("Bag not found"));
        return mapBag(bag);
    }

    @Override
    public List<BagResponse> getBagsBySourceStore(UUID storeId) {
        // This is a naive implementation, ideally we'd add a method in repository findBySourceStoreId
        return bagRepository.findAll().stream()
                .filter(b -> b.getSourceStore() != null && b.getSourceStore().getId().equals(storeId))
                .map(this::mapBag)
                .collect(Collectors.toList());
    }

    @Override
    public BagResponse updateBagStatus(UUID bagId, String newStatus) {
        Bag bag = bagRepository.findById(bagId).orElseThrow(() -> new RuntimeException("Bag not found"));
        bag.setStatus(newStatus);
        
        // Auto-update internal garments based on bag status
        if ("RECEIVED_AT_HQ".equals(newStatus)) {
            processGarmentsInBag(bagId, GarmentStatus.RECEIVED_AT_HQ.name());
        } else if ("UNPACKED_AT_STORE".equals(newStatus)) {
            processGarmentsInBag(bagId, GarmentStatus.DROPPED_AT_STORE.name());
        }
        
        return mapBag(bagRepository.save(bag));
    }

    @Override
    public BagResponse processGarmentsInBag(UUID bagId, String targetStatus) {
        Bag bag = bagRepository.findById(bagId).orElseThrow(() -> new RuntimeException("Bag not found"));
        List<Garment> garments = garmentRepository.findAll().stream()
                .filter(g -> g.getCurrentBag() != null && g.getCurrentBag().getId().equals(bagId))
                .collect(Collectors.toList());
                
        for (Garment g : garments) {
            g.setStatus(GarmentStatus.valueOf(targetStatus));
        }
        garmentRepository.saveAll(garments);
        return mapBag(bag);
    }

    private BagResponse mapBag(Bag bag) {
        return BagResponse.builder()
                .id(bag.getId())
                .qrCode(bag.getQrCode())
                .sourceStoreId(bag.getSourceStore() != null ? bag.getSourceStore().getId() : null)
                .sourceStoreName(bag.getSourceStore() != null ? bag.getSourceStore().getName() : null)
                .destinationStoreId(bag.getDestinationStore() != null ? bag.getDestinationStore().getId() : null)
                .destinationStoreName(bag.getDestinationStore() != null ? bag.getDestinationStore().getName() : null)
                .status(bag.getStatus())
                .totalGarments(bag.getGarments() != null ? bag.getGarments().size() : 0)
                .createdAt(bag.getCreatedAt())
                .updatedAt(bag.getUpdatedAt())
                .build();
    }
}
