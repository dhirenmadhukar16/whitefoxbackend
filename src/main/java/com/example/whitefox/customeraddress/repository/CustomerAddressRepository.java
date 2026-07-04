package com.example.whitefox.customeraddress.repository;



import com.example.whitefox.customeraddress.entity.CustomerAddress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CustomerAddressRepository
        extends JpaRepository<CustomerAddress, UUID> {

    List<CustomerAddress> findByCustomerId(UUID customerId);
}
