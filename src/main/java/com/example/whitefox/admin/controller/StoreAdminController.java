package com.example.whitefox.admin.controller;

import com.example.whitefox.admin.entity.Machine;
import com.example.whitefox.admin.entity.StoreExpense;
import com.example.whitefox.admin.repository.MachineRepository;
import com.example.whitefox.admin.repository.StoreExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin-panel/storeadmin")
@CrossOrigin("*")
public class StoreAdminController {

    @Autowired
    private MachineRepository machineRepository;

    @Autowired
    private StoreExpenseRepository storeExpenseRepository;

    @GetMapping("/machines")
    public ResponseEntity<List<Machine>> getAllMachines() {
        return ResponseEntity.ok(machineRepository.findAll());
    }

    @GetMapping("/expenses")
    public ResponseEntity<List<StoreExpense>> getAllExpenses() {
        return ResponseEntity.ok(storeExpenseRepository.findAll());
    }
}
