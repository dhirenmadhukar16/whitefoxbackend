package com.example.whitefox.adminpanel.service;

import com.example.whitefox.store.entity.Store;
import com.example.whitefox.store.entity.StoreExpense;
import com.example.whitefox.store.entity.StoreMachine;
import com.example.whitefox.store.repository.StoreExpenseRepository;
import com.example.whitefox.store.repository.StoreMachineRepository;
import com.example.whitefox.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StoreAdminServiceImpl implements StoreAdminService {

    private final StoreMachineRepository machineRepository;
    private final StoreExpenseRepository expenseRepository;
    private final StoreRepository storeRepository;

    @Override
    public List<StoreMachine> getMachinesByStoreId(UUID storeId) {
        return machineRepository.findByStoreId(storeId);
    }

    @Override
    public StoreMachine addMachine(UUID storeId, StoreMachine machine) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new RuntimeException("Store not found"));
        machine.setStore(store);
        return machineRepository.save(machine);
    }

    @Override
    public List<StoreExpense> getExpensesByStoreId(UUID storeId) {
        return expenseRepository.findByStoreIdOrderByDateDesc(storeId);
    }

    @Override
    public StoreExpense addExpense(UUID storeId, StoreExpense expense) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new RuntimeException("Store not found"));
        expense.setStore(store);
        return expenseRepository.save(expense);
    }
}
