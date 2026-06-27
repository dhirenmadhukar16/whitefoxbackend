package com.example.whitefox.trucklogistics.repository;

import com.example.whitefox.trucklogistics.entity.TruckManifestItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TruckManifestItemRepository extends JpaRepository<TruckManifestItem, UUID> {

    List<TruckManifestItem> findByManifestId(UUID manifestId);

    List<TruckManifestItem> findByQrCode(String qrCode);
}