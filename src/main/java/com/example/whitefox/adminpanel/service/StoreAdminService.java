package com.example.whitefox.adminpanel.service;

import com.example.whitefox.store.entity.StoreExpense;
import com.example.whitefox.store.entity.StoreMachine;

import java.util.List;
import java.util.UUID;

public interface StoreAdminService {
    List<StoreMachine> getMachinesByStoreId(UUID storeId);
    StoreMachine addMachine(UUID storeId, StoreMachine machine);
    
    List<StoreExpense> getExpensesByStoreId(UUID storeId);
    StoreExpense addExpense(UUID storeId, StoreExpense expense);
}
