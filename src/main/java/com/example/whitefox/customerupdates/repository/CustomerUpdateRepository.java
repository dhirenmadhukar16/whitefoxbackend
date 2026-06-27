package com.example.whitefox.customerupdates.repository;


import com.example.whitefox.customerupdates.entity.CustomerUpdate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CustomerUpdateRepository
        extends JpaRepository<CustomerUpdate, UUID> {

    List<CustomerUpdate> findByCustomerIdOrderByCreatedAtDesc(
            UUID customerId
    );

    List<CustomerUpdate> findByOrderIdOrderByCreatedAtAsc(
            UUID orderId
    );
}
