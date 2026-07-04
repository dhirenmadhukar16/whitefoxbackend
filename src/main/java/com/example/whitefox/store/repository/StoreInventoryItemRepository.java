package com.example.whitefox.store.repository;

import com.example.whitefox.store.entity.StoreInventoryItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface StoreInventoryItemRepository extends JpaRepository<StoreInventoryItem, UUID> {
    List<StoreInventoryItem> findByStoreId(UUID storeId);
}
