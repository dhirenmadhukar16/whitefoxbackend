package com.example.whitefox.orders.repository;

import java.util.List;
import java.util.UUID;

import com.example.whitefox.orders.entity.LaundryOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface LaundryOrderRepository
        extends JpaRepository<LaundryOrder, UUID> {

    Optional<LaundryOrder> findByOrderNumber(
            String orderNumber
    );
    List<LaundryOrder> findByDeliveryRiderId(UUID riderId);
    List<LaundryOrder> findByStoreId(UUID storeId);
}