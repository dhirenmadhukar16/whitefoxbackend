package com.example.whitefox.customerbooking.repository;



import com.example.whitefox.customerbooking.entity.CustomerBookingItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CustomerBookingItemRepository
        extends JpaRepository<CustomerBookingItem, UUID> {

    List<CustomerBookingItem> findByBookingId(UUID bookingId);
}
