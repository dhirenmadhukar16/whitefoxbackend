package com.example.whitefox.adminpanel.controller;

import com.example.whitefox.adminpanel.service.StoreAdminService;
import com.example.whitefox.store.entity.StoreExpense;
import com.example.whitefox.store.entity.StoreMachine;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin-panel/storeadmin")
@RequiredArgsConstructor
public class StoreAdminController {

    private final StoreAdminService storeAdminService;

    @GetMapping("/machines")
    public List<StoreMachine> getMachines(@RequestParam UUID storeId) {
        return storeAdminService.getMachinesByStoreId(storeId);
    }

    @PostMapping("/machines")
    public StoreMachine addMachine(@RequestParam UUID storeId, @RequestBody StoreMachine machine) {
        return storeAdminService.addMachine(storeId, machine);
    }

    @GetMapping("/expenses")
    public List<StoreExpense> getExpenses(@RequestParam UUID storeId) {
        return storeAdminService.getExpensesByStoreId(storeId);
    }

    @PostMapping("/expenses")
    public StoreExpense addExpense(@RequestParam UUID storeId, @RequestBody StoreExpense expense) {
        return storeAdminService.addExpense(storeId, expense);
    }
}
