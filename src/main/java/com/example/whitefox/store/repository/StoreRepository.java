package com.example.whitefox.store.repository;

import com.example.whitefox.store.entity.Store;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface StoreRepository extends JpaRepository<Store, UUID> {

    Optional<Store> findByStoreCode(String storeCode);
    Optional<Store> findById(UUID id);

}