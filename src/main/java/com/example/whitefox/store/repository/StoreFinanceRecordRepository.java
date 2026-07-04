package com.example.whitefox.store.repository;

import com.example.whitefox.store.entity.StoreFinanceRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StoreFinanceRecordRepository extends JpaRepository<StoreFinanceRecord, UUID> {
    List<StoreFinanceRecord> findByStoreIdOrderByRecordDateDesc(UUID storeId);
    Optional<StoreFinanceRecord> findByStoreIdAndRecordDate(UUID storeId, LocalDate recordDate);
}
