package com.example.whitefox.store.repository;

import com.example.whitefox.store.entity.StoreExpense;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Repository;

@Repository
public interface StoreExpenseRepository extends JpaRepository<StoreExpense, UUID> {
    List<StoreExpense> findByStoreIdOrderByDateDesc(UUID storeId);
}
