package com.example.whitefox.admin.controller;

import com.example.whitefox.admin.entity.WarehouseInventory;
import com.example.whitefox.admin.entity.PurchaseOrder;
import com.example.whitefox.admin.repository.WarehouseInventoryRepository;
import com.example.whitefox.admin.repository.PurchaseOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin-panel/warehouse")
@CrossOrigin("*")
public class WarehouseController {

    @Autowired
    private WarehouseInventoryRepository inventoryRepository;

    @Autowired
    private PurchaseOrderRepository poRepository;

    @GetMapping("/inventory")
    public ResponseEntity<List<WarehouseInventory>> getInventory() {
        return ResponseEntity.ok(inventoryRepository.findAll());
    }

    @PostMapping("/inventory")
    public ResponseEntity<WarehouseInventory> addInventory(@RequestBody WarehouseInventory item) {
        return ResponseEntity.ok(inventoryRepository.save(item));
    }

    @GetMapping("/purchase-orders")
    public ResponseEntity<List<PurchaseOrder>> getPurchaseOrders() {
        return ResponseEntity.ok(poRepository.findAll());
    }
}
