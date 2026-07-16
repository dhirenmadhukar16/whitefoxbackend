package com.example.whitefox.store.repository;

import com.example.whitefox.store.entity.StoreMachine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface StoreMachineRepository extends JpaRepository<StoreMachine, UUID> {
    List<StoreMachine> findByStoreId(UUID storeId);
}
