package com.example.whitefox.finance.controller;

import com.example.whitefox.finance.model.OperatingExpense;
import com.example.whitefox.finance.repository.OperatingExpenseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/finance/expenses")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class OperatingExpenseController {

    private final OperatingExpenseRepository repository;

    @GetMapping
    public ResponseEntity<List<OperatingExpense>> getAllExpenses() {
        return ResponseEntity.ok(repository.findAll());
    }

    @PostMapping
    public ResponseEntity<OperatingExpense> addExpense(@RequestBody OperatingExpense expense) {
        OperatingExpense saved = repository.save(expense);
        return ResponseEntity.ok(saved);
    }
}
