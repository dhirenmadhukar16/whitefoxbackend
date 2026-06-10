package com.example.whitefox.orders.repository;



import com.example.whitefox.orders.entity.LaundryOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface LaundryOrderRepository
        extends JpaRepository<LaundryOrder, UUID> {

    Optional<LaundryOrder> findByOrderNumber(
            String orderNumber
    );
}