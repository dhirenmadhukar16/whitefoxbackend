package com.example.whitefox.admin.controller;

import com.example.whitefox.admin.entity.Machine;
import com.example.whitefox.admin.entity.StoreExpense;
import com.example.whitefox.admin.repository.MachineRepository;
import com.example.whitefox.admin.repository.StoreExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("adminModuleStoreAdminController")
@RequestMapping("/api/admin/storeadmin")
@CrossOrigin("*")
public class StoreAdminController {

    @Autowired
    private MachineRepository machineRepository;

    @Autowired
    private StoreExpenseRepository storeExpenseRepository;

    @GetMapping("/machines")
    public ResponseEntity<List<Machine>> getAllMachines(@RequestParam(required = false) java.util.UUID storeId) {
        if (storeId != null) {
            return ResponseEntity.ok(machineRepository.findAll().stream().filter(m -> storeId.equals(m.getStoreId())).toList());
        }
        return ResponseEntity.ok(machineRepository.findAll());
    }

    @GetMapping("/expenses")
    public ResponseEntity<List<StoreExpense>> getAllExpenses(@RequestParam(required = false) java.util.UUID storeId) {
        if (storeId != null) {
            return ResponseEntity.ok(storeExpenseRepository.findAll().stream().filter(e -> storeId.equals(e.getStoreId())).toList());
        }
        return ResponseEntity.ok(storeExpenseRepository.findAll());
    }
}
