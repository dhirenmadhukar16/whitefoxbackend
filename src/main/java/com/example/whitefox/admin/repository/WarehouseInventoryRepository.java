package com.example.whitefox.admin.repository;

import com.example.whitefox.admin.entity.WarehouseInventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface WarehouseInventoryRepository extends JpaRepository<WarehouseInventory, UUID> {
}
