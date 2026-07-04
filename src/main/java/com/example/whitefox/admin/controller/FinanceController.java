package com.example.whitefox.admin.controller;

import com.example.whitefox.admin.entity.Settlement;
import com.example.whitefox.admin.entity.CompanyExpense;
import com.example.whitefox.admin.repository.SettlementRepository;
import com.example.whitefox.admin.repository.CompanyExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin-panel/finance")
@CrossOrigin("*")
public class FinanceController {

    @Autowired
    private SettlementRepository settlementRepository;

    @Autowired
    private CompanyExpenseRepository companyExpenseRepository;

    @GetMapping("/settlements")
    public ResponseEntity<List<Settlement>> getAllSettlements() {
        return ResponseEntity.ok(settlementRepository.findAll());
    }

    @GetMapping("/company-expenses")
    public ResponseEntity<List<CompanyExpense>> getCompanyExpenses() {
        return ResponseEntity.ok(companyExpenseRepository.findAll());
    }
}
