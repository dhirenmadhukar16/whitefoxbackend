package com.example.whitefox.trucklogistics.repository;

import com.example.whitefox.trucklogistics.entity.TruckManifest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TruckManifestRepository extends JpaRepository<TruckManifest, UUID> {

    List<TruckManifest> findByTripId(UUID tripId);

    List<TruckManifest> findBySourceStoreId(UUID storeId);

    List<TruckManifest> findByDestinationStoreId(UUID storeId);
}