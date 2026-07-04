package com.example.whitefox.finance.repository;

import com.example.whitefox.finance.model.OperatingExpense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OperatingExpenseRepository extends JpaRepository<OperatingExpense, UUID> {
}
