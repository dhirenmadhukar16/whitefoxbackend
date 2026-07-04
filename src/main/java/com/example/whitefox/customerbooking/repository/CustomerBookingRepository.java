package com.example.whitefox.customerbooking.repository;


import com.example.whitefox.customerbooking.entity.CustomerBooking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CustomerBookingRepository
        extends JpaRepository<CustomerBooking, UUID> {

    List<CustomerBooking> findByCustomerId(UUID customerId);

    List<CustomerBooking> findByStoreId(UUID storeId);

    List<CustomerBooking> findByAssignedRiderId(UUID riderId);

//    List<CustomerBooking> findByAssignedRiderId(UUID riderId);
}
