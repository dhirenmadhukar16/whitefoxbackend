package com.example.whitefox.store.repository;

import com.example.whitefox.store.entity.StoreServicePricing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StoreServicePricingRepository extends JpaRepository<StoreServicePricing, UUID> {
    List<StoreServicePricing> findByStoreId(UUID storeId);
    Optional<StoreServicePricing> findByStoreIdAndServiceCatalogId(UUID storeId, UUID serviceCatalogId);
}
