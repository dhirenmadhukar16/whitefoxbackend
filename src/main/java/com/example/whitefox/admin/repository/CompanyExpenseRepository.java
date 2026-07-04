package com.example.whitefox.admin.repository;

import com.example.whitefox.admin.entity.CompanyExpense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface CompanyExpenseRepository extends JpaRepository<CompanyExpense, UUID> {
}
