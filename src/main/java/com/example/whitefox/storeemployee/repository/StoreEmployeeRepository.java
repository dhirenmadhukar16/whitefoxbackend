package com.example.whitefox.storeemployee.repository;



import com.example.whitefox.storeemployee.entity.StoreEmployee;
import com.example.whitefox.storeemployee.enums.StoreEmployeeStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StoreEmployeeRepository
        extends JpaRepository<StoreEmployee, UUID> {

    List<StoreEmployee> findByStoreId(UUID storeId);

    List<StoreEmployee> findByStoreIdAndStatus(
            UUID storeId,
            StoreEmployeeStatus status
    );

    Optional<StoreEmployee> findByPhone(String phone);
}
