package com.example.whitefox.customerreview.repository;

//package com.example.whitefox.customerreview.repository;

import com.example.whitefox.customerreview.entity.CustomerReview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CustomerReviewRepository
        extends JpaRepository<CustomerReview, UUID> {

    List<CustomerReview> findByCustomerId(UUID customerId);

    List<CustomerReview> findByOrderId(UUID orderId);

    List<CustomerReview> findByStoreId(UUID storeId);

    List<CustomerReview> findByRiderId(UUID riderId);
}
