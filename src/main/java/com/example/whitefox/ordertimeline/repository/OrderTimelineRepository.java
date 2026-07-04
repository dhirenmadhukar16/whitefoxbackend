package com.example.whitefox.ordertimeline.repository;

import com.example.whitefox.ordertimeline.entity.OrderTimeline;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OrderTimelineRepository
        extends JpaRepository<OrderTimeline, UUID> {

    List<OrderTimeline> findByOrderIdOrderByActionTimeAsc(UUID orderId);

}