package com.example.whitefox.customers.repository;

//package com.example.whitefox.customer.repository;

import com.example.whitefox.customers.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository
        extends JpaRepository<Customer, UUID> {

    Optional<Customer> findByPhone(String phone);
    Optional<Customer> findById(UUID id);
    boolean existsByPhone(String phone);
}
