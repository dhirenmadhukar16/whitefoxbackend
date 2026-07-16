package com.example.whitefox.admin.repository;

import com.example.whitefox.admin.entity.StoreExpense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository("adminStoreExpenseRepository")
public interface StoreExpenseRepository extends JpaRepository<StoreExpense, UUID> {
}
